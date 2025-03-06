package com.mio.mailclient.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/*Gestisce la logica di interazione con la view.
* Esegue operazione come la connessione al server,
* la gestione dei messaggi in arrivo
* e l'invio di email*/
public class ClientController {
    @FXML
    private Button btnNewMail;

    @FXML
    public void onNewMailButtonClick() throws IOException {
        newStage("NewMail");
    }
    @FXML
    public void onNewMailboxClick() throws IOException {
        newStage("Mailbox");
    }

    private void newStage(String fileName) throws IOException {
        Stage stage = (Stage)btnNewMail.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mio/mailclient/"+fileName+".fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Scrivi mail");
        stage.setScene(scene);
        //stage.setOnCloseRequest();
        stage.show();

    }
}
