package model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*Gestisce l'autenticazione e connessione e invio di messaggi*/
public class MailServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {

        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            //il server è pronto ad accettare la connessione del client
            while(true) {
                System.out.println("Server in ascolto");
                Socket clientSocket = serverSocket.accept();

                System.out.println("client connesso" + clientSocket.getInetAddress());

                //ottiene gli stream di input e output per comunicare con il cliente
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();
                ObjectInputStream objectReader = new ObjectInputStream(input);

                //comunichiamo con il client attraverso questi oggetti
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                PrintWriter writer = new PrintWriter(output,true);

                //Controllo che l'indirizzo mail esista
                String response ="";
                String sender = reader.readLine();
                while(!emailExists(sender)){
                    response = "Indirizzo email inesistente";
                    writer.println(response);
                    sender = reader.readLine();
                }


                if(emailExists(sender)) {
                    response = "L'utente è autenticato";
                    writer.println(response);
                    /*for(Email e : FileManager.readEmailFromMailbox(email)){
                        System.out.println(e.toString());
                    }*/
                    //Dopo il controllo sul mittente invio la mail
                    Email e = (Email) objectReader.readObject();
                    ArrayList<String> validRecipients = new ArrayList<>();
                    for (String recipient : e.getRecipients()) {
                        if (emailExists(recipient)) {
                            validRecipients.add(recipient);
                            response = "Email inviata a: " + recipient;
                            writer.println(response);
                        } else {
                            response = "Destinatario inesistente: " + recipient;
                            writer.println(response);
                        }
                    }
                    e.setRecipients(validRecipients);
                    if (validRecipients.isEmpty()) {
                        System.out.println("Non hai inserito destinatari validi");
                    } else {
                        FileManager.writeEmailToMailbox(e);
                    }
                }
                clientSocket.close();
                System.out.println("connessione chiusa");

            }
        }catch(IOException e){
            System.out.println("Errore nella connessione" + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean emailExists(String email){
        File file = new File ("src/main/Mailbox/" + email + ".txt");
        return file.exists();
    }
}