package com.mio.mailserver.controller;

import com.mio.mailclient.model.Email;
import com.mio.mailclient.model.Request;
import com.mio.mailclient.model.Response;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static com.mio.mailserver.model.FileManager.*;

public class RunnableServer implements Runnable{
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
                        objectWriter.writeObject(authenticate(request.getSender()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "MAILBOX":
                    try {
                        objectWriter.writeObject(sendMailbox(request.getSender()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "DETAIL":
                    try {
                        objectWriter.writeObject(viewDetail(request.getSender(), request.getId()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "SEND":
                    try {
                        objectWriter.writeObject(sendEmail(request.getEmail()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "DELETE":
                    try {
                        objectWriter.writeObject(deleteEmail(request.getSender(), request.getId()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
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
        for(Email email : mailbox){
            if(email.getId()==id){
                mailbox.remove(email);
            }
        }
        writeMailbox(mailbox, user);
        return new Response(null, "Mail eliminata con successo", null);

    }

    private static Response sendEmail(Email email) throws IOException {
        ArrayList<String> recipients = email.getRecipients();
        for(String recipient : recipients){
            if(emailExists(recipient)){
                writeEmailToMailbox(email, recipient);
            }
        }
        return new Response(null, "Mail inviata con successo(forse)", null);
    }

    private static Response viewDetail(String sender, int id) throws IOException {
        return new Response(null, null, readEmailFromMailbox(sender, id));
    }


    private static Response sendMailbox(String sender) throws IOException {
        return new Response(getMailbox(sender), null, null);

    }

    private static Response authenticate(String sender){
        if (emailExists(sender)) {
           return new Response(null,"OK", null);
        } else {
            return new Response(null,"KO", null);
        }
    }

    private static boolean emailExists(String sender){
        File file = new File ("MailServer/src/main/Mailbox/" + sender + ".txt");
        return file.exists();
    }

}
