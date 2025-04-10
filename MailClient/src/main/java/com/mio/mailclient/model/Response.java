package com.mio.mailclient.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {

    private String message;
    private ArrayList<Email> mailbox;
    private Email email;
    private boolean newMails;

    public Response(ArrayList<Email> mailbox, String message, Email email, boolean newMails) {
        this.mailbox = mailbox;
        this.message = message;
        this.email =email;
        this.newMails = newMails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Email> getMailbox() {
        return mailbox;
    }

    public void setMailbox(ArrayList<Email> mailbox) {
        this.mailbox = mailbox;
    }

    public boolean hasNewMails(){return newMails;}

    public void setNewMails(boolean newMails){
        this.newMails = newMails;
    }


}
