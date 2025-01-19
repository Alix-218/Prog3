package model;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*Gestisce la connessione al server e invio e ricezione dei messaggi*/
public class MailClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) throws IOException {
        //controlliamo il mittente sintatticamente prima di aprire la connessione
        String sender = checkSender();

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connesso al server");

            //Ottiene gli stream di Input e Output per la comunicazione con il serve
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            PrintWriter writer = new PrintWriter(output, true);
            ObjectOutputStream objectWriter = new ObjectOutputStream(output);

            //Invio la mail mittente per l'autenticazione al server
            writer.println(sender);
            //Leggo il risultato del server
            String serverMessage = reader.readLine();
            System.out.println(serverMessage);
            //Se il mittente non esiste lo inserisco di nuovo e lo ricontrollo sintatticamnete
            while(serverMessage.equals("Indirizzo email inesistente")){
                sender  = checkSender();
                writer.println(sender);
                serverMessage = reader.readLine();
                System.out.println(serverMessage);
            }


            //Invio la mia email
            Email e = createEmail(sender);
            objectWriter.writeObject(e);
            objectWriter.flush();
            System.out.println("Email inviata al server");
            //Leggo il risultato del server più volte avendo più destinatari
            while((serverMessage = reader.readLine())!= null){
                System.out.println(serverMessage);
            }


        } catch (IOException e) {
            System.out.println("Errore di connessione" + e.getMessage());

        }


    }

    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@[a-zA-Z]+(?:\\.[a-zA-Z]+)*(?:\\.(com|net|org|it))+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            System.out.println("Indirizzo email non corretto");
        }
        return matcher.matches();
    }

    //controllo per mittente
    public static String checkSender() throws IOException {
        String sender = " ";
        do{
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Inserisci mail: ");
            sender = userInput.readLine();
        }while(!isValidEmail(sender));
        return sender;
    }
    //controllo per destinatari
    public static ArrayList<String> checkRecipients() throws IOException {
        ArrayList<String> recipients = new ArrayList<>();
        String recipient = "";
        boolean flag = true;
        while (flag || recipients.isEmpty()) {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Inserisci destinatari: ");
            recipient = userInput.readLine();
            if (recipient.equals("FINE")) {
                flag = false;
                if(recipients.isEmpty()) {
                    System.out.println("Devi inserire almeno un destinatario");
                }
            } else if (isValidEmail(recipient)) {
                recipients.add(recipient);
                flag = true;
            } else {
                System.out.println("Indirizzo destinatario non valido: " + recipient);
                flag=true;
            }
        }
        return recipients;

    }
    //Creo la mail
    public static Email createEmail(String sender) throws IOException {
        Email e = new Email();
        e.setId(0);
        e.setSender(sender);
        e.setRecipients(checkRecipients());
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Inserisci oggetto: ");
        String topic = userInput.readLine();
        e.setTopic(topic);
        userInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Inserisci testo: ");
        String text = userInput.readLine();
        e.setText(text);
        return e;
    }

}
