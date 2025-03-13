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

public class BuyPokeLabSaucesController extends GraphicController {
    @FXML
    private ImageView teriyakiImage;

    @FXML
    private ImageView soyImage;

    @FXML
    private ImageView wasabiImage;

    private BuyPokeLabAppController pokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;

    public void initialize(int id, PokeLabBean pokeLabBean){
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
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
    }
}
