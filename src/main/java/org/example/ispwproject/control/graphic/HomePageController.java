package org.example.ispwproject.control.graphic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.ispwproject.ChangePage;


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
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml");
    }

    @FXML
    public void handlePostOnPokeWall(ActionEvent event) {
        System.out.println("Posta su pokè wall");
    }

}
