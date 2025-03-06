module com.example.yourpokeispwproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.yourpokeispwproject to javafx.fxml;
    exports com.example.yourpokeispwproject;
}