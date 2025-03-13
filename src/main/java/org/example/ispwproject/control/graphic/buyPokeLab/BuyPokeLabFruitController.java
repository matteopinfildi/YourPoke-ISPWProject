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

public class BuyPokeLabFruitController  extends GraphicController{
    @FXML
    private ImageView avocadoImage;

    @FXML
    private ImageView mangoImage;

    @FXML
    private ImageView strawbarriesImage;

    private BuyPokeLabAppController pokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;

    public void initialize(int id, PokeLabBean pokeLabBean){
        Image avocado = new Image(getClass().getResource("/org/example/ispwproject/image/avocado.jpg").toExternalForm());
        avocadoImage.setImage(avocado);

        Image mango = new Image(getClass().getResource("/org/example/ispwproject/image/mango.jpg").toExternalForm());
        mangoImage.setImage(mango);

        Image strawbarries = new Image(getClass().getResource("/org/example/ispwproject/image/strawbarries.jpeg").toExternalForm());
        strawbarriesImage.setImage(strawbarries);
    }


    @FXML
    private VBox avocadoNutritionalValues;

    @FXML
    private VBox mangoNutritionalValues;

    @FXML
    private VBox strawbarriesNutritionalValues;

    @FXML
    private void toggleAvocadoNutritional() {
        avocadoNutritionalValues.setVisible(!avocadoNutritionalValues.isVisible());
    }

    @FXML
    private void toggleMangoNutritional() {
        mangoNutritionalValues.setVisible(!mangoNutritionalValues.isVisible());
    }

    @FXML
    private void toggleStrawbarriesNutritional() {
        strawbarriesNutritionalValues.setVisible(!strawbarriesNutritionalValues.isVisible());
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
