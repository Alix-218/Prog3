package model;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;


public class FileManager {

    public static ArrayList<Email> readEmailFromMailbox(String sender) throws IOException {
        ArrayList<Email> mailbox = new ArrayList<>();
        FileReader file = new FileReader("src/main/Mailbox/" + sender + ".txt");
        BufferedReader reader = new BufferedReader(file);
        String line = "";
        while((line = reader.readLine())!= null){
            String[] fields = line.split(",");
            Email email = new Email ();
            email.setId(Integer.parseInt(fields[0]));
            email.setSender(fields[1]);
            email.setRecipients(new ArrayList<String>(Arrays.asList(fields[2].split(";"))));
            email.setTopic(fields[3]);
            email.setText(fields[4]);
            email.setSentDate(LocalDateTime.parse(fields[5]));
            mailbox.add(email);
        }

        return mailbox;
    }

//TODO scrivere su file della mailbox

    public static void writeEmailToMailbox(Email email) throws IOException {
        String dataEmail = " ";
        for(String recipient : email.getRecipients()) {
            FileWriter writeEmail = new FileWriter("src/main/Mailbox/" + recipient + ".txt", true);
            BufferedWriter writer = new BufferedWriter(writeEmail);

            dataEmail = email.getId() + ", " +
                    email.getSender() + ", " +
                    String.join("; ", email.getRecipients()) + ", " +
                    email.getTopic() + ", " +
                    email.getText() + ", " +
                    email.getSentDate();
            writer.write(dataEmail);
            writer.newLine();
            writer.close();
        }

    }


}
