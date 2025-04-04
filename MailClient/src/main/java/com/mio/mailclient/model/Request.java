package com.mio.mailclient.model;

import java.io.Serializable;

public class Request implements Serializable {
    private String sender;
    private String operation;
    private int id;
    private Email email;

    public Request(String sender, String operation){
        this.sender=sender;
        this.operation=operation;
    }

    public Request(Email email, String operation){
        this.email=email;
        this.operation=operation;
    }

    public Request(String sender, int id, String operation){
        this.sender=sender;
        this.id=id;
        this.operation=operation;
    }

    public int getId(){
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getOperation() {
        return operation;
    }

    public Email getEmail(){
        return email;
    }


}
