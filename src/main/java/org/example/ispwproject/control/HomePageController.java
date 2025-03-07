package org.example.ispwproject.control;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class HomePageController {
    @FXML
    private ImageView bannerImage;
    public void initialize() {
        try {
            Image banner = new Image(getClass().getResource("/org/example/ispwproject/image/banner.JPG").toExternalForm());
            bannerImage.setImage(banner);
        } catch (NullPointerException e) {
            System.err.println("Impossibile trovare il file banner.jpg. Controlla il percorso.");
        }
    }
}
