package org.example.ispwproject.control.graphic.buyPokeLab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.graphic.GraphicController;

public class BuyPokeLabSaucesController  {
    @FXML
    private ImageView teriyakiImage;

    @FXML
    private ImageView soyImage;

    @FXML
    private ImageView wasabiImage;

    public void initialize(){
        Image teriyaki = new Image(getClass().getResource("/org/example/ispwproject/image/teriyaki.jpg").toExternalForm());
        teriyakiImage.setImage(teriyaki);

        Image soy = new Image(getClass().getResource("/org/example/ispwproject/image/soy.jpg").toExternalForm());
        soyImage.setImage(soy);

        Image wasabi = new Image(getClass().getResource("/org/example/ispwproject/image/wasabi.jpg").toExternalForm());
        wasabiImage.setImage(wasabi);
    }


    @FXML
    private VBox teriyakiNutritionalValues;

    @FXML
    private VBox soyNutritionalValues;

    @FXML
    private VBox wasabiNutritionalValues;

    @FXML
    private void toggleTeriyakiNutritional() {
        teriyakiNutritionalValues.setVisible(!teriyakiNutritionalValues.isVisible());
    }

    @FXML
    private void toggleSoyNutritional() {
        soyNutritionalValues.setVisible(!soyNutritionalValues.isVisible());
    }

    @FXML
    private void toggleWasabiNutritional() {
        wasabiNutritionalValues.setVisible(!wasabiNutritionalValues.isVisible());
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
