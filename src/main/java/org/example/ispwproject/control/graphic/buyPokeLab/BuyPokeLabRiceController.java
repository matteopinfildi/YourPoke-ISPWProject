package org.example.ispwproject.control.graphic.buyPokeLab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.ispwproject.ChangePage;

public class BuyPokeLabRiceController {

    @FXML
    private ImageView sushiRiceImage;

    @FXML
    private ImageView venusRiceImage;

    @FXML
    private ImageView basmatiRiceImage;

    public void initialize(){
        Image sushiRice = new Image(getClass().getResource("/org/example/ispwproject/image/sushiRice.jpeg").toExternalForm());
        sushiRiceImage.setImage(sushiRice);

        Image venusRice = new Image(getClass().getResource("/org/example/ispwproject/image/venusRice.jpg").toExternalForm());
        venusRiceImage.setImage(venusRice);

        Image basmatiRice = new Image(getClass().getResource("/org/example/ispwproject/image/basmatiRice.jpeg").toExternalForm());
        basmatiRiceImage.setImage(basmatiRice);
    }


    @FXML
    private VBox sushiNutritionalValues;

    @FXML
    private VBox venusNutritionalValues;

    @FXML
    private VBox basmatiNutritionalValues;

    @FXML
    private void toggleSushiNutritional() {
        sushiNutritionalValues.setVisible(!sushiNutritionalValues.isVisible());
    }

    @FXML
    private void toggleVenusNutritional() {
        venusNutritionalValues.setVisible(!venusNutritionalValues.isVisible());
    }

    @FXML
    private void toggleBasmatiNutritional() {
        basmatiNutritionalValues.setVisible(!basmatiNutritionalValues.isVisible());
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
