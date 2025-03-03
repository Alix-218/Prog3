package com.mio.mailserver.controller;



/*Gestisce le richieste in entrata dal client come
  invio di messaggi, ricezione di email
  e la gestione della connessione
 */


import com.mio.mailserver.model.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

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
    private Server server;

    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private TextArea logArea;

    public void initialize() throws IOException {
        this.server = new Server();
        startServer();
        btnStart.setDisable(true);
        btnStop.setDisable(false);
    }




    @FXML
    public void startServer() throws IOException {

        try{
            serverSocket = new ServerSocket(PORT);
            //il server è pronto ad accettare la connessione del client
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        exec = Executors.newFixedThreadPool(NUM_THREAD);
        server.setRunning(true);
        btnStart.setDisable(true);
        btnStop.setDisable(false);

        new Thread(()->{

            while(server.isRunning()){
                System.out.println("Server in ascolto");

                Socket clientSocket = null;
                try {
                    clientSocket = serverSocket.accept();
                    RunnableServer runnableServer = new RunnableServer(clientSocket);
                    exec.execute(runnableServer);
                    System.out.println("client connesso" + clientSocket.getInetAddress());
                } catch (IOException e) {
                    System.out.println("server chiuso");;
                }

            }
        }).start();
    }

    @FXML
    public void stopServer() throws IOException, InterruptedException {
        server.setRunning(false);
        exec.shutdown();

        try {
            serverSocket.close();
        }catch (IOException e){
            System.out.println("abbiamo un problema");
        }

        btnStart.setDisable(false);
        btnStop.setDisable(true);
    }
}
