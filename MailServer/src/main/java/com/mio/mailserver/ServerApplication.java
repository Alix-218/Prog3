package com.mio.mailserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class ServerApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("serverGUI.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Il mio server");
        stage.setScene(scene);
        //stage.setOnCloseRequest();


        stage.show();

    }

    public static void main(String[] args) {

        launch();

    }
}
