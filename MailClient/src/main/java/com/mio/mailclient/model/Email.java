package com.mio.mailclient.model;
/*Struttura di un email, gestisce la validazione degli indirizzi*/

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Email implements Serializable{
    private static final long serialVersionUID = 1L;
    private int id;
    private String sender;
    private ArrayList<String> recipients;
    private String topic;
    private String text;
    private LocalDateTime sentDate;


    public Email(int id, String sender, ArrayList<String> recipients, String topic, String text, LocalDateTime sentDate) {
        this.id = id;
        this.sender = sender;
        this.recipients = recipients;
        this.topic = topic;
        this.text = text;
        this.sentDate = sentDate;



    }

    public Email() {
    }



    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public ArrayList<String> getRecipients() {
        return recipients;
    }

    public String getTopic() {
        return topic;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setRecipients(ArrayList<String> recipients) {
        this.recipients = recipients;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }
}

    