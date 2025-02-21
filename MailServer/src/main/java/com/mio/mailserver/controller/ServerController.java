package com.mio.mailserver.controller;



/*Gestisce le richieste in entrata dal client come
  invio di messaggi, ricezione di email
  e la gestione della connessione
 */


import javafx.fxml.FXML;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerController {
    private static final int PORT = 12345;
    private static final int NUM_THREAD = 3;
    private ServerSocket serverSocket;
    private ExecutorService exec;
    boolean flag = true;

    @FXML
    public void startServer() throws IOException {

        try{
            serverSocket = new ServerSocket(PORT);
            //il server è pronto ad accettare la connessione del client
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        exec = Executors.newFixedThreadPool(NUM_THREAD);

        new Thread(()->{
            while(flag){
                System.out.println("Server in ascolto");

                Socket clientSocket = null;
                try {
                    clientSocket = serverSocket.accept();
                } catch (IOException e) {
                    System.out.println("server chiuso");;
                }
                RunnableServer runnableServer = new RunnableServer();
                exec.execute(runnableServer);

                System.out.println("client connesso" + clientSocket.getInetAddress());
            }
        }).start();
    }

    @FXML
    public void stopServer() throws IOException, InterruptedException {
        flag = false;
        exec.shutdown();

        exec.awaitTermination(5, TimeUnit.SECONDS);

        try {
            serverSocket.close();
        }catch (IOException e){
            System.out.println("abbiamo un problema");
        }
    }
}
