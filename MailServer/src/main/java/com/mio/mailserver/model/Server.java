package com.mio.mailserver.model;

public class Server {

    boolean running;

    public Server(){
        running = true;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
