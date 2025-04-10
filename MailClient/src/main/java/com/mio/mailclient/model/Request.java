package com.mio.mailclient.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {
    private String user;
    private String operation;
    private int id;
    private Email email;
    private ArrayList<Email> mailbox;

    public Request(){

    }
    public int getId(){
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getOperation() {
        return operation;
    }

    public Email getEmail(){
        return email;
    }

    public ArrayList<Email> getMailbox(){return mailbox;}

    public void setUser(String user) {
        this.user = user;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setMailbox(ArrayList<Email> mailbox) {
        this.mailbox = mailbox;
    }
}
