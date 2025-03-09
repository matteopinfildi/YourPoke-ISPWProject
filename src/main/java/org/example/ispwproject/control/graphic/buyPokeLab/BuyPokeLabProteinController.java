package org.example.ispwproject.control.graphic.buyPokeLab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.ispwproject.ChangePage;

public class BuyPokeLabProteinController {

    @FXML
    private ImageView salmonImage;

    @FXML
    private ImageView shrimpImage;

    @FXML
    private ImageView tunaImage;

    public void initialize(){
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
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml");
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml");
    }
}
