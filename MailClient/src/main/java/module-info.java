module com.mio.mailclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mio.mailclient to javafx.graphics;
    exports com.mio.mailclient;

    opens com.mio.mailclient.controller to javafx.fxml;
    exports com.mio.mailclient.controller;
    exports com.mio.mailclient.model;
}


