package com.mio.mailclient.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {

    private String message;
    private ArrayList<Email> mailBox;
    private Email email;

    public Response(ArrayList<Email> mailBox, String message, Email email) {
        this.mailBox = mailBox;
        this.message = message;
        this.email =email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Email> getMailBox() {
        return mailBox;
    }

    public void setMailBox(ArrayList<Email> mailBox) {
        this.mailBox = mailBox;
    }
}
