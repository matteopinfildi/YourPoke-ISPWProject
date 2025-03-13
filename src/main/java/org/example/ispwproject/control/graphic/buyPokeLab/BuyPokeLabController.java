package org.example.ispwproject.control.graphic.buyPokeLab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.graphic.GraphicController;

public class BuyPokeLabController {

    @FXML
    private ImageView riceImage;

    @FXML
    private ImageView proteinImage;

    @FXML
    private ImageView fruitImage;

    @FXML
    private ImageView crunchyImage;

    @FXML
    private ImageView saucesImage;

    @FXML
    private ImageView extraImage;

    public void initialize() {

        Image rice = new Image(getClass().getResource("/org/example/ispwproject/image/rice.jpg").toExternalForm());
        riceImage.setImage(rice);

        Image protein = new Image(getClass().getResource("/org/example/ispwproject/image/protein.jpeg").toExternalForm());
        proteinImage.setImage(protein);

        Image fruit = new Image(getClass().getResource("/org/example/ispwproject/image/fruit.jpg").toExternalForm());
        fruitImage.setImage(fruit);

        Image crunchy = new Image(getClass().getResource("/org/example/ispwproject/image/crunchy.jpg").toExternalForm());
        crunchyImage.setImage(crunchy);

        Image sauces = new Image(getClass().getResource("/org/example/ispwproject/image/sauces.jpg").toExternalForm());
        saucesImage.setImage(sauces);

//        Image extra = new Image(getClass().getResource("/org/example/ispwproject/image/extra.jpg").toExternalForm());
//        extraImage.setImage(extra);
    }

    @FXML
    public void handleRice(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabRice.fxml");
    }

    @FXML
    public void handleProtein(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabProtein.fxml");
    }

    @FXML
    public void handleFruit(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabFruit.fxml");
    }

    @FXML
    public void handleCrunchy(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabCrunchy.fxml");
    }

    @FXML
    public void handleSauces(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabSauces.fxml");
    }

    @FXML
    public void handleExtra(ActionEvent event) {
        System.out.println("Extra");
    }

    @FXML
    public void handleAddNameClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addName.fxml");
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/homePage.fxml");
    }
}
