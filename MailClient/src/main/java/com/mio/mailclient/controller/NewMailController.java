package com.mio.mailclient.controller;

import com.mio.mailclient.model.Email;
import com.mio.mailclient.model.Request;
import com.mio.mailclient.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import static com.mio.mailclient.controller.LoginController.isValidEmail;

public class NewMailController {
    @FXML
    public Button btnSendMail;
    @FXML
    private TextField recipientsField;
    @FXML
    private Label msgErr;
    @FXML
    private TextField loggedSender;
    @FXML
    private TextField topicField;
    @FXML
    private TextArea textArea;

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private ObjectInputStream objectReader;
    private ObjectOutputStream objectWriter;
    private User user;

    private String getRecipientsStringFromArrayList(ArrayList<String> recipients){
        StringBuilder temp = new StringBuilder();
        for(String recipient : recipients) {
            temp.append(recipient).append(";");
        }
        return temp.substring(0,temp.length()-1).trim();
    }

    public void initialize(User loggedUser, Email email, String op){
        switch(op){
            case "REPLY":
                user = loggedUser;
                loggedSender.setText(user.getUsername());
                recipientsField.setText(getRecipientsStringFromArrayList(email.getRecipients()));
                break;
            case "REPLYALL":
                user = loggedUser;
                loggedSender.setText(user.getUsername());
                recipientsField.setText(getRecipientsStringFromArrayList(email.getRecipients()));
                topicField.setText(email.getTopic());
                break;

            case "NEWMAIL":
                user = loggedUser;
                loggedSender.setText(user.getUsername());
                break;

            case "DETAIL":
                user = loggedUser;
                loggedSender.setText(email.getSender());
                recipientsField.setText(getRecipientsStringFromArrayList(email.getRecipients()));
                topicField.setText(email.getTopic());
                textArea.setText(email.getText());
                btnSendMail.setVisible(false);
                break;

            default :
                break;
        }
    }




    public void onSendMailButtonClick() throws IOException, ClassNotFoundException {
        Email email = new Email();
        email.setSender(user.getUsername());
        email.setRecipients(new ArrayList<>(Arrays.asList(recipientsField.getText().split(";"))));
        email.setTopic(topicField.getText());
        email.setText(textArea.getText());
        email.setSentDate(email.getSentDate());
        sendMail(email);

    }

    public void sendMail(Email email) throws IOException, ClassNotFoundException {
        boolean valid= true;
        String[] recipients = recipientsField.getText().split(";");
        for(String recipient : recipients) {
            if (!isValidEmail(recipient)) {
                recipientsField.setText("");
                msgErr.setVisible(true);
                valid=false;
            }
        }
        if(valid){
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            objectWriter = new ObjectOutputStream(socket.getOutputStream());
            objectWriter.writeObject(createRequestForSend(email, "SEND"));
            closeWindow();

        }
    }

    private void closeWindow() throws IOException {
        Stage stage = (Stage)btnSendMail.getScene().getWindow();
        stage.close();

    }


    private static Request createRequestForSend(Email email, String operation){
        Request request = new Request();
        request.setEmail(email);
        request.setOperation(operation);
        return request;

    }


}
