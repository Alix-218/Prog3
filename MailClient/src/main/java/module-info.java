module com.mio.mailclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mio.mailclient to javafx.fxml;
    exports com.mio.mailclient;
}