package org.example.ispwproject.control.graphic.buyPokeLab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;

public class ExtraIngredientController extends GraphicController {
    @FXML
    private ImageView octopusImage;

    @FXML
    private ImageView pineappleImage;

    @FXML
    private ImageView nachosImage;

    @FXML
    private ImageView mayoImage;

    private BuyPokeLabAppController pokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;

    public void initialize(int id, PokeLabBean pokeLabBean){
        Image octopus = new Image(getClass().getResource("/org/example/ispwproject/image/octopus.jpg").toExternalForm());
        octopusImage.setImage(octopus);

        Image pineapple = new Image(getClass().getResource("/org/example/ispwproject/image/pineapple.jpeg").toExternalForm());
        pineappleImage.setImage(pineapple);

        Image nachos = new Image(getClass().getResource("/org/example/ispwproject/image/nachos.jpg").toExternalForm());
        nachosImage.setImage(nachos);

        Image mayo = new Image(getClass().getResource("/org/example/ispwproject/image/mayo.jpg").toExternalForm());
        mayoImage.setImage(mayo);
    }
    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/extra.fxml", pokeLabBean, id);
    }
}
