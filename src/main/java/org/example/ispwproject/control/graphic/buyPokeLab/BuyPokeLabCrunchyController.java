package org.example.ispwproject.control.graphic.buyPokeLab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;

public class BuyPokeLabCrunchyController  extends GraphicController{
    @FXML
    private ImageView onionImage;

    @FXML
    private ImageView nutsImage;

    @FXML
    private ImageView almondsImage;

    private BuyPokeLabAppController pokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;

    public void initialize(int id, PokeLabBean pokeLabBean){
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
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
    }
}
