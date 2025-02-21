module com.mio.mailserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mio.mailserver to javafx.graphics;
    exports com.mio.mailserver;

    opens com.mio.mailserver.controller to javafx.fxml;
    exports com.mio.mailserver.controller;
}