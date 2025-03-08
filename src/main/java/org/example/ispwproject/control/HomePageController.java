package org.example.ispwproject.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class HomePageController {
    @FXML
    private ImageView bannerImage;

    @FXML
    private ImageView postPWImage;

    @FXML
    private ImageView buyCPImage;

    @FXML
    private ImageView buyPLImage;

    public void initialize() {
            Image banner = new Image(getClass().getResource("/org/example/ispwproject/image/banner.JPG").toExternalForm());
            bannerImage.setImage(banner);

            Image pokeWall = new Image(getClass().getResource("/org/example/ispwproject/image/pokeWall.JPG").toExternalForm());
            postPWImage.setImage(pokeWall);

            Image classicPoke = new Image(getClass().getResource("/org/example/ispwproject/image/buyClassicPoke.JPG").toExternalForm());
            buyCPImage.setImage(classicPoke);

            Image pokeLab = new Image(getClass().getResource("/org/example/ispwproject/image/buyPokeLab.JPG").toExternalForm());
            buyPLImage.setImage(pokeLab);
    }

    @FXML
    public void handleBuyPokeLab(ActionEvent event) {
        System.out.println("Crea il tuo pokè!");
    }

    @FXML
    public void handlePostOnPokeWall(ActionEvent event) {
        System.out.println("Posta su pokè wall");
    }

}
