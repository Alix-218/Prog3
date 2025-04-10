package com.mio.mailclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Il mio client");
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> {
            System.out.println("chiusura");
        });

        stage.show();



    }



    public static void main(String[] args) {

        launch();

    }
}
