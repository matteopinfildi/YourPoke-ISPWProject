package org.example.ispwproject.control.graphic.buyPokeLab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BuyPokeLabController {

    @FXML
    private ImageView riceImage;

    public void initialize() {
        Image rice = new Image(getClass().getResource("/org/example/ispwproject/image/rice.jpg").toExternalForm());
        riceImage.setImage(rice);
    }

    @FXML
    public void handleRice(ActionEvent event) {
        System.out.println("Risoooo");
    }
}
