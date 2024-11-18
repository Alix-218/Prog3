module com.example.serverprog3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.serverprog3 to javafx.fxml;
    exports com.example.serverprog3;
}