package model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/*Gestisce l'autenticazione e connessione e invio di messaggi*/
public class MailServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {

        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server in ascolto");
            //il server è pronto ad accettare la connessione del client
            while(true) {
                Socket clientSocket = serverSocket.accept();

                System.out.println("client connesso" + clientSocket.getInetAddress());

                //ottiene gli stream di input e output per comunicare con il cliente
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();

                //comunichiamo con il client attraverso questi oggetti
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                PrintWriter writer = new PrintWriter(output, true);

                //Controllo che l'indirizzo mail esista

                String email = reader.readLine();

                if (emailExists(email)) {
                    String response = "L'utente è autenticato";
                    writer.println(response);
                    for(Email e : FileManager.readEmail(email)){
                        System.out.println(e.toString());
                    }
                } else {
                    String response = "Indirizzo email inesistente";
                    writer.println(response);
                }


                clientSocket.close();
                System.out.println("connessione chiusa.");
            }
        }catch(IOException e){
            System.out.println("Errore nella connessione" + e.getMessage());
        }
    }


    public static boolean emailExists(String email){
        File file = new File ("src/main/Mailbox/" + email + ".txt");
        return file.exists();
    }
}
