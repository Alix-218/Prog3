package com.mio.mailclient.controller;

import com.mio.mailclient.model.Email;
import com.mio.mailclient.model.Request;

import com.mio.mailclient.model.Response;
import com.mio.mailclient.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private Label messageError;
    @FXML
    private Button btnLogin;

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private ObjectInputStream objectReader;
    private ObjectOutputStream objectWriter;





    public void onLoginButtonClick() throws IOException, ClassNotFoundException {
        String email = username.getText();
        if(!isValidEmail(email)){
            username.setText("");
            messageError.setVisible(true);
        }else{
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            objectWriter = new ObjectOutputStream(socket.getOutputStream());
            objectReader = new ObjectInputStream(socket.getInputStream());
            objectWriter.writeObject(createRequest(email,"AUTH"));
            Response response = (Response)objectReader.readObject();
            if(response.getMessage().equals("OK")){
                newStage(response.getMailBox());
            }else{
                messageError.setText("Utente inesistente");
                messageError.setVisible(true);
            }

        }

    }

    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@[a-zA-Z]+(?:\\.[a-zA-Z]+)*(?:\\.(com|net|org|it))+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            System.out.println("Indirizzo email non corretto");
        }
        return matcher.matches();
    }

    private static Request createRequest(String user, String operation){
        return new Request(user, operation);

    }

    @FXML
    private void newStage(ArrayList<Email> mailbox) throws IOException {
        Stage stage = (Stage)btnLogin.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mio/mailclient/Mailbox.fxml"));
        Parent root = loader.load();
        MailboxController mailboxController = loader.getController();
        User user = new User(username.getText(), mailbox);
        mailboxController.setUser(user);
        Scene scene = new Scene(root);
        stage.setTitle("Il mio client");
        stage.setScene(scene);
        //stage.setOnCloseRequest();
        stage.show();

    }


}
