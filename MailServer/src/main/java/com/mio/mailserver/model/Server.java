package com.mio.mailserver.model;

import javafx.beans.property.SimpleStringProperty;

public class Server {

    boolean running;
    private SimpleStringProperty property;



    public Server(){
        running = true;
        property = new SimpleStringProperty("");
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public synchronized void writeLog(String log){
        String s = property.get() + log;
        property.set(s);
    }

    public SimpleStringProperty getProperty(){
        return property;
    }
}
