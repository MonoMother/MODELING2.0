module com.example.strashno {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.strashno to javafx.fxml;
    exports com.example.strashno;
}