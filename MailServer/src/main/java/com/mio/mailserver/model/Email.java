package com.mio.mailserver.model;


import java.time.LocalDateTime;
import java.util.ArrayList;

import java.io.Serializable;

/* Come client, ma qui serve per gestire i messaggi sul server (mailbox)*/
public class Email implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String sender;
    private ArrayList<String> recipients;
    private String topic;
    private String text;
    private LocalDateTime sentDate;

    public Email() {
    }

    public Email(int id, String sender, ArrayList<String> recipients, String topic, String text){
        this.id=id;
        this.sender = sender;
        this.recipients=recipients;
        this.topic=topic;
        this.text=text;
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

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", recipients=" + recipients +
                ", topic='" + topic + '\'' +
                ", text='" + text + '\'' +
                ", sentDate=" + sentDate +
                '}';
    }
}
