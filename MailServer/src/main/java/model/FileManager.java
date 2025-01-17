package model;

import javafx.util.converter.LocalDateTimeStringConverter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager {

    public static ArrayList<Email> readEmail(String mittente) throws IOException {
        ArrayList<Email> mailbox = new ArrayList<>();
        FileReader file = new FileReader("src/main/Mailbox/" + mittente + ".txt");
        BufferedReader reader = new BufferedReader(file);
        String line = "";
        while((line = reader.readLine())!= null){
            String[] fields = line.split(",");
            Email email = new Email ();
            email.setId(Integer.parseInt(fields[0]));
            email.setMittente(fields[1]);
            email.setDestinatari(new ArrayList<String>(Arrays.asList(fields[2].split(";"))));
            email.setOggetto(fields[3]);
            email.setTesto(fields[4]);
            email.setDataInvio(LocalDateTime.parse(fields[5]));
            mailbox.add(email);
        }

        return mailbox;
    }

//TODO scrivere su file della mailbox

}
