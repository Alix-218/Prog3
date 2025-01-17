module com.mio.mailserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mio.mailserver to javafx.fxml;
    exports com.mio.mailserver;
}