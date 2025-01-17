module model {
    requires javafx.controls;
    requires javafx.fxml;


    opens model to javafx.fxml;
    exports model;
}