package com.mio.mailclient.controller;

import com.mio.mailclient.model.Email;
import com.mio.mailclient.model.Request;
import com.mio.mailclient.model.Response;
import com.mio.mailclient.model.User;
import com.mio.mailclient.utils.EmailCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MailboxController {
    @FXML
    private ListView<Email> emailListView;

    @FXML
    private Label senderLabel;

    @FXML
    private Label subjectLabel;

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private ObjectInputStream objectReader;
    private ObjectOutputStream objectWriter;

    private ObservableList<Email> emailList = FXCollections.observableArrayList();
    private User user;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public void setUser(User user){
        this.user=user;
    }

    @FXML
    public void init() {
        emailListView.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        // Simuliamo l'arrivo di email
        getMailboxFromServer();
        emailListView.setItems(emailList);
        emailListView.setCellFactory(param -> new EmailCell());
        startScheduler();
    }

    public void getMailboxFromServer() {
        new Thread(()->{
            createConnection();
            try {
                objectWriter.writeObject(createRequestForMailbox(user.getUsername() ,"MAILBOX"));
                Response response = (Response)objectReader.readObject();
                emailList.addAll(response.getMailbox());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    public void startScheduler(){
        scheduler.scheduleAtFixedRate(() -> {
            try {
                ArrayList<Email> temp = new ArrayList<>(emailListView.getItems());
                Request request = createRequestForPollingMail(temp, "GET_NEW_MAILS", user.getUsername());
                Response response = sendRequestToServer(request);

                if (response.hasNewMails()) {
                    // aggiorna la view della mailbox
                    Platform.runLater(() -> {
                            emailList.addAll(response.getMailbox());

                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5, 5, TimeUnit.SECONDS); // primo polling subito, poi ogni 5 secondi
    }



    private Response sendRequestToServer(Request request) throws IOException, ClassNotFoundException {
        createConnection();
        Response response = null;
        try{
            objectWriter.writeObject(request);
            response = (Response)objectReader.readObject();
        }catch(EOFException e){
            System.out.println(e.getMessage());
        }
        return response;

    }

    private void createConnection(){
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            objectWriter = new ObjectOutputStream(socket.getOutputStream());
            objectReader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Request createRequestForMailbox(String user, String operation){
        Request request = new Request();
        request.setUser(user);
        request.setOperation(operation);
        return request;
    }
    private static Request createRequestForPollingMail(ArrayList<Email> mailbox, String operation, String user){
        Request request = new Request();
        request.setMailbox(mailbox);
        request.setUser(user);
        request.setOperation(operation);
        return request;
    }

    private static Request createRequestForDelete(String user, int id, String operation){
        Request request = new Request();
        request.setUser(user);
        request.setId(id);
        request.setOperation(operation);
        return request;
    }

    @FXML
    public void handleNewMail() throws IOException {
        newStage(new Email(),"NEWMAIL");
    }


    @FXML
    public void handleEmailClick(MouseEvent e) throws IOException {
        if(e.getClickCount()==2){
            handleDetail();
        }else {
            Email selectedEmail = emailListView.getSelectionModel().getSelectedItem();
            if (selectedEmail != null) {
                senderLabel.setText(selectedEmail.getSender());
                subjectLabel.setText(selectedEmail.getTopic());
            }
        }

    }

    @FXML
    public void handleDetail() throws IOException {
        Email selectedEmail = emailListView.getSelectionModel().getSelectedItem();
        if (selectedEmail != null) {
            Email email = new Email();
            email.setSender(selectedEmail.getSender());
            email.setRecipients(selectedEmail.getRecipients());
            email.setTopic(selectedEmail.getTopic());
            email.setText(selectedEmail.getText());
            newStage(email, "DETAIL");
        }

    }

    @FXML
    private void handleReply() throws IOException {
        Email selectedEmail = emailListView.getSelectionModel().getSelectedItem();
        if (selectedEmail != null) {
            System.out.println("Rispondi a: " + selectedEmail.getSender());
            Email email = new Email();
            ArrayList<String> temp = new ArrayList<>();
            temp.add(selectedEmail.getSender());
            email.setSender(user.getUsername());
            email.setRecipients(temp);
            email.setTopic(selectedEmail.getTopic());
            newStage(email, "REPLY");
        }

    }

    @FXML
    private void handleReplyAll() throws IOException {
        Email selectedEmail = emailListView.getSelectionModel().getSelectedItem();
        if (selectedEmail != null) {
            ArrayList<String> recipients = new ArrayList<>();
            recipients.add(selectedEmail.getSender().trim());
            for(String recipient : selectedEmail.getRecipients()){
                if(!(recipient.trim().equals(user.getUsername()))){
                    recipients.add(recipient.trim());
                }
            }

            Email email = new Email();
            email.setSender(user.getUsername());
            email.setRecipients(recipients);
            email.setTopic(selectedEmail.getTopic());

            newStage(email,"REPLYALL");
        }


    }

    @FXML
    private void handleDelete() {
        Email selectedEmail = emailListView.getSelectionModel().getSelectedItem();
        if (selectedEmail != null) {
            createConnection();
            try {
                objectWriter.writeObject(createRequestForDelete(user.getUsername(), selectedEmail.getId(), "DELETE"));
                Response response = (Response)objectReader.readObject();
                if(response.getMessage().equals("OK")){
                    emailList.remove(selectedEmail);
                    System.out.println("Email eliminata!");
                }

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


        }
    }

    private void newStage(Email email, String op) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mio/mailclient/NewMail.fxml"));
        Parent root = loader.load();
        NewMailController newMailController = loader.getController();
        newMailController.initialize(user, email, op);
        Scene scene = new Scene(root);
        stage.setTitle("Il mio client");
        stage.setScene(scene);
        //stage.setOnCloseRequest();
        stage.show();
    }


}
