package com.mio.mailserver.controller;



/*Gestisce le richieste in entrata dal client come
  invio di messaggi, ricezione di email
  e la gestione della connessione
 */


import com.mio.mailclient.model.Email;
import com.mio.mailclient.model.Request;
import com.mio.mailclient.model.Response;
import com.mio.mailserver.model.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.mio.mailserver.model.FileManager.*;
import static com.mio.mailserver.model.FileManager.getMailbox;

public class ServerController {
    private static final int PORT = 12345;
    private static final int NUM_THREAD = 3;
    private ServerSocket serverSocket;
    private ExecutorService exec;
    private Server server;

    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private TextArea logArea;

    public void initialize() throws IOException {
        this.server = new Server();
        startServer();
        btnStart.setDisable(true);
        btnStop.setDisable(false);
    }

    @FXML
    public void startServer() throws IOException {

        try{
            serverSocket = new ServerSocket(PORT);
            //il server è pronto ad accettare la connessione del client
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        exec = Executors.newFixedThreadPool(NUM_THREAD);
        server.setRunning(true);
        btnStart.setDisable(true);
        btnStop.setDisable(false);

        new Thread(()->{

            while(server.isRunning()){
                System.out.println("Server in ascolto");

                Socket clientSocket = null;
                try {
                    clientSocket = serverSocket.accept();
                    RunnableServer runnableServer = new RunnableServer(clientSocket);
                    logArea.textProperty().bind(server.getProperty());
                    exec.execute(runnableServer);
                    System.out.println("client connesso" + clientSocket.getInetAddress());
                } catch (IOException e) {
                    System.out.println("server chiuso");;
                }

            }
        }).start();
    }

    @FXML
    public void stopServer() throws IOException, InterruptedException {
        server.setRunning(false);
        exec.shutdown();

        try {
            serverSocket.close();
        }catch (IOException e){
            System.out.println("abbiamo un problema");
        }

        btnStart.setDisable(false);
        btnStop.setDisable(true);
    }

    private class RunnableServer implements Runnable{
        private Socket socket;
        private ObjectInputStream objectReader;
        private ObjectOutputStream objectWriter;


        public RunnableServer(Socket socket) {
            this.socket = socket;
            try{
                objectReader = new ObjectInputStream(socket.getInputStream());
                objectWriter = new ObjectOutputStream(socket.getOutputStream());
            }catch (IOException e){
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            try {
                Request request = (Request) objectReader.readObject();
                //Finché non inserisco un indirizzo esistente continuo ad aspettares
                switch(request.getOperation()){
                    case "AUTH":
                        try {
                            server.writeLog("[INFO] Richiesta di autenticazione da : " + request.getSender() + "\n");
                            objectWriter.writeObject(authenticate(request.getSender()));
                            server.writeLog("[INFO] Risposta inviata\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "MAILBOX":
                        try {
                            server.writeLog("[INFO] Richiesta di mailbox da : " + request.getSender() + "\n");
                            objectWriter.writeObject(sendMailbox(request.getSender()));
                            server.writeLog("[INFO] Risposta inviata \n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "SEND":
                        try {
                            server.writeLog("[INFO] Richiesta di invio mail\n");
                            objectWriter.writeObject(sendEmail(request.getEmail()));
                            server.writeLog("[INFO] Risposta inviata\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "DELETE":
                        try {
                            server.writeLog("[INFO] Richiesta di eliminazione mail : " + request.getId()+ " dal client : " +request.getSender()+ "\n");
                            objectWriter.writeObject(deleteEmail(request.getSender(), request.getId()));
                            server.writeLog("[INFO] Risposta inviata \n");
                        } catch (IOException e) {
                            server.writeLog("[ERROR]\n");
                            throw new RuntimeException(e);
                        }
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    socket.close();
                    objectWriter.close();
                    objectReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


        }

        private static Response deleteEmail(String user, int id) throws IOException{
            ArrayList<Email> mailbox = getMailbox(user);
            mailbox.removeIf(email -> email.getId() == id);
            writeMailbox(mailbox, user);
            return new Response(null, "OK", null);

        }

        private static Response sendEmail(Email email) throws IOException {
            ArrayList<String> recipients = email.getRecipients();
            for(String recipient : recipients){
                if(emailExists(recipient)){
                    email.setId(getMailbox(recipient).size()+1);
                    writeEmailToMailbox(email, recipient);
                }else{
                    email.setId(getMailbox(email.getSender()).size()+1);
                    writeEmailToMailbox(createErrorMail(email, recipient), email.getSender());
                }
            }
            return new Response(null, "OK", null);
        }

        private static Email createErrorMail(Email email, String oldRecipient){
            ArrayList<String> recipients =new ArrayList<>();
            recipients.add(email.getSender());
            Email errorEmail = new Email();
            errorEmail.setId(email.getId());
            errorEmail.setSender("server@mail.com");
            errorEmail.setRecipients(recipients);
            errorEmail.setTopic("Errore nell'invio della mail");
            errorEmail.setText("Indirizzo mail non valido: " + oldRecipient );
            errorEmail.setSentDate(errorEmail.getSentDate());
            return errorEmail;
        }

        private static Response sendMailbox(String sender) throws IOException {
            return new Response(getMailbox(sender), null, null);

        }

        private static Response authenticate(String sender) throws IOException {
            if (emailExists(sender)) {
                return new Response(getMailbox(sender),"OK", null);
            } else {
                return new Response(null,"KO", null);
            }
        }

        private static boolean emailExists(String sender){
            File file = new File ("MailServer/src/main/Mailbox/" + sender + ".txt");
            return file.exists();

        }



    }

}
