package model;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*Gestisce la connessione al server e invio e ricezione dei messaggi*/
public class MailClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) throws IOException {
        String email = " ";
        do{
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Inserisci mail: ");
            email = userInput.readLine();
        }while(!isValidEmail(email));

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connesso al server");

            //Ottiene gli stream di Input e Output per la comunicazione con il serve
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            PrintWriter writer = new PrintWriter(output, true);

            //Invio la mail per l'autenticazione al server

            writer.println(email);


            //Leggo il risultato del server
            String serverMessage = reader.readLine();
            System.out.println(serverMessage);

            if (serverMessage.equals("L'utente è autenticato")) {
                System.out.println("sei dentro");
            } else {
                System.out.println("L'utente non esiste");
            }

        } catch (IOException e) {
            System.out.println("Errore di connessione" + e.getMessage());

        }


    }

    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@[a-zA-Z]+(?:\\.[a-zA-Z]+)*(?:\\.(com|net|org|it))+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
