package com.mio.mailclient.model;

import java.util.ArrayList;

public class User {
    private String username;
    private ArrayList<Email> mailbox;

    public User(String username, ArrayList<Email> mailbox) {
        this.username = username;
        this.mailbox = mailbox;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Email> getMailbox() {
        return mailbox;
    }

    public void setMailbox(ArrayList<Email> mailbox) {
        this.mailbox = mailbox;
    }
}
