package org.example.ispwproject.control.graphic.buyPokeLab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.ispwproject.ChangePage;

public class BuyPokeLabCrunchyController {
    @FXML
    private ImageView onionImage;

    @FXML
    private ImageView nutsImage;

    @FXML
    private ImageView almondsImage;

    public void initialize(){
        Image onion = new Image(getClass().getResource("/org/example/ispwproject/image/onion.jpg").toExternalForm());
        onionImage.setImage(onion);

        Image nuts = new Image(getClass().getResource("/org/example/ispwproject/image/nuts.jpg").toExternalForm());
        nutsImage.setImage(nuts);

        Image almonds = new Image(getClass().getResource("/org/example/ispwproject/image/almonds.jpg").toExternalForm());
        almondsImage.setImage(almonds);
    }


    @FXML
    private VBox onionNutritionalValues;

    @FXML
    private VBox nutsNutritionalValues;

    @FXML
    private VBox almondsNutritionalValues;

    @FXML
    private void toggleOnionNutritional() {
        onionNutritionalValues.setVisible(!onionNutritionalValues.isVisible());
    }

    @FXML
    private void toggleNutsNutritional() {
        nutsNutritionalValues.setVisible(!nutsNutritionalValues.isVisible());
    }

    @FXML
    private void toggleAlmondsNutritional() {
        almondsNutritionalValues.setVisible(!almondsNutritionalValues.isVisible());
    }


    @FXML
    public void handleNextClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml");
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml");
    }
}
