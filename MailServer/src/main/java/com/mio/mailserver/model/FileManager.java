package com.mio.mailserver.model;

import com.mio.mailclient.model.Email;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;



public class FileManager {

    public static ArrayList<Email> getMailbox(String sender) throws IOException {
        ArrayList<Email> mailbox = new ArrayList<>();
        FileReader file = new FileReader("src/main/Mailbox/" + sender + ".txt");

        try(BufferedReader reader = new BufferedReader(file)) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                Email email = new Email();
                email.setId(Integer.parseInt(fields[0]));
                email.setSender(fields[1]);
                email.setRecipients(new ArrayList<String>(Arrays.asList(fields[2].split(";"))));
                email.setTopic(fields[3]);
                email.setText(fields[4]);
                email.setSentDate(LocalDateTime.parse(fields[5]));
                mailbox.add(email);
            }
        }

        return mailbox;
    }

    public static Email readEmailFromMailbox(String sender, int id) throws IOException {
        Email email = new Email();
        FileReader file = new FileReader("src/main/Mailbox/" + sender + ".txt");


        try(BufferedReader reader = new BufferedReader(file)) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if(Integer.parseInt(fields[0])==id) {
                    email.setId(Integer.parseInt(fields[0]));
                    email.setSender(fields[1]);
                    email.setRecipients(new ArrayList<String>(Arrays.asList(fields[2].split(";"))));
                    email.setTopic(fields[3]);
                    email.setText(fields[4]);
                    email.setSentDate(LocalDateTime.parse(fields[5]));
                }
            }
        }

        return email;
    }


    public static void writeEmailToMailbox(Email email, String recipient) throws IOException {
        //Scrivo a ogni mailbox destinataria

        FileWriter writeEmail = new FileWriter("src/main/Mailbox/" + recipient + ".txt", true);
        try(BufferedWriter writer = new BufferedWriter(writeEmail)) {

            String dataEmail = email.getId() + ", " +
                    email.getSender() + ", " +
                    String.join("; ", email.getRecipients()) + ", " +
                    email.getTopic() + ", " +
                    email.getText() + ", " +
                    email.getSentDate();
            writer.write(dataEmail);
            writer.newLine();
        }


    }

    public static void writeMailbox(ArrayList<Email> mailbox, String user) throws IOException {
        FileWriter writeEmail = new FileWriter("src/main/Mailbox/" + user + ".txt", false);
        try(BufferedWriter writer = new BufferedWriter(writeEmail)) {
            for(Email email : mailbox){
                String dataEmail = email.getId() + ", " +
                        email.getSender() + ", " +
                        String.join("; ", email.getRecipients()) + ", " +
                        email.getTopic() + ", " +
                        email.getText() + ", " +
                        email.getSentDate();
                writer.write(dataEmail);
                writer.newLine();
            }
        }

    }


}
