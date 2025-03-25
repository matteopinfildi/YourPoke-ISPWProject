module org.example.ispwproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.ispwproject to javafx.fxml;
    exports org.example.ispwproject;
    exports org.example.ispwproject.control.graphic;
    opens org.example.ispwproject.control.graphic to javafx.fxml;
    exports org.example.ispwproject.control.graphic.buypokelab;
    opens org.example.ispwproject.control.graphic.buypokelab to javafx.fxml;
    exports org.example.ispwproject.control.graphic.pokewall;
    opens org.example.ispwproject.control.graphic.pokewall to javafx.fxml;
}