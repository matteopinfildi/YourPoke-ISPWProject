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

public class BuyPokeLabProteinController extends GraphicController {

    @FXML
    private ImageView salmonImage;

    @FXML
    private ImageView shrimpImage;

    @FXML
    private ImageView tunaImage;

    private BuyPokeLabAppController pokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;

    public void initialize(int id, PokeLabBean pokeLabBean){
        Image salmon = new Image(getClass().getResource("/org/example/ispwproject/image/salmon.jpg").toExternalForm());
        salmonImage.setImage(salmon);

        Image shrimp = new Image(getClass().getResource("/org/example/ispwproject/image/shrimp.jpg").toExternalForm());
        shrimpImage.setImage(shrimp);

        Image tuna = new Image(getClass().getResource("/org/example/ispwproject/image/tuna.jpg").toExternalForm());
        tunaImage.setImage(tuna);
    }


    @FXML
    private VBox salmonNutritionalValues;

    @FXML
    private VBox shrimpNutritionalValues;

    @FXML
    private VBox tunaNutritionalValues;

    @FXML
    private void toggleSalmonNutritional() {
        salmonNutritionalValues.setVisible(!salmonNutritionalValues.isVisible());
    }

    @FXML
    private void toggleShrimpNutritional() {
        shrimpNutritionalValues.setVisible(!shrimpNutritionalValues.isVisible());
    }

    @FXML
    private void toggleTunaNutritional() {
        tunaNutritionalValues.setVisible(!tunaNutritionalValues.isVisible());
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
