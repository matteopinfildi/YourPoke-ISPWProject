module org.example.ispwproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.ispwproject.control to javafx.fxml;
    exports org.example.ispwproject;
}