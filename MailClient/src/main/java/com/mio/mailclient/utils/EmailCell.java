package com.mio.mailclient.utils;

import com.mio.mailclient.model.Email;
import javafx.scene.control.ListCell;

public class EmailCell extends ListCell<Email> {
    @Override
    protected void updateItem(Email email, boolean empty) {
        super.updateItem(email, empty);
        if (empty || email == null) {
            setText(null);
        } else {
            setText(email.getSender() + " - " + email.getTopic() + " (" + email.getSentDate() + ")");
        }
    }
}
