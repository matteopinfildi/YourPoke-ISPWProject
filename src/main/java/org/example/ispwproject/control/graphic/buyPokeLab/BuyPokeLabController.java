package org.example.ispwproject.control.graphic.buyPokeLab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.enumeration.ingredient.*;
import org.example.ispwproject.utils.exception.SystemException;

public class BuyPokeLabController extends GraphicController{

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

    @FXML private Button riceButton;
    @FXML private Button proteinButton;
    @FXML private Button fruitButton;
    @FXML private Button crunchyButton;
    @FXML private Button saucesButton;

    private BuyPokeLabAppController pokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;

    @FXML private Label totalPriceLabel;

    // per vedere poke vecchi potrebbe servirmi questo, DA SISTEMARE!
    // private static boolean toRecover = false;

    // sistemare le eccezioni, mancano IO, Login e SQL
    public void initialize(int id, PokeLabBean pokeLabBean) throws SystemException {

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

        pokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = id;

        if(totalPriceLabel != null && pokeLabBean != null){
            totalPriceLabel.setText("TOT = " + pokeLabBean.getPrice() + "$");

            GenericAlternative genericAlternative;

            genericAlternative = pokeLabBean.getIngredient("rice");
            if(genericAlternative != null){
                riceButton.setText(((RiceAlternative)genericAlternative).name());
            }

            genericAlternative = pokeLabBean.getIngredient("protein");
            if(genericAlternative != null){
                riceButton.setText(((ProteinAlternative)genericAlternative).name());
            }

            genericAlternative = pokeLabBean.getIngredient("fruit");
            if(genericAlternative != null){
                riceButton.setText(((FruitAlternative)genericAlternative).name());
            }

            genericAlternative = pokeLabBean.getIngredient("crunchy");
            if(genericAlternative != null){
                riceButton.setText(((CrunchyAlternative)genericAlternative).name());
            }

            genericAlternative = pokeLabBean.getIngredient("sauces");
            if(genericAlternative != null){
                riceButton.setText(((SaucesAlternative)genericAlternative).name());
            }
        }

        //manca la parte per recuperare i vecchi poke, da vedere!
    }

    @FXML
    public void handleRice(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabRice.fxml", pokeLabBean, id);
    }

    @FXML
    public void handleProtein(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabProtein.fxml", pokeLabBean, id);
    }

    @FXML
    public void handleFruit(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabFruit.fxml", pokeLabBean, id);
    }

    @FXML
    public void handleCrunchy(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabCrunchy.fxml", pokeLabBean, id);
    }

    @FXML
    public void handleSauces(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabSauces.fxml", pokeLabBean, id);
    }


    @FXML
    public void handleAddNameClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addName.fxml", pokeLabBean, id);
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/homePage.fxml", pokeLabBean, id);
    }
}
