package org.example.ispwproject.control.graphic.buypokelab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.enumeration.ingredient.*;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

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
    @FXML private Button addNameButton;
    @FXML private Button bowlSizeButton;
    @FXML private Label sizeLabel;

    private BuyPokeLabAppController pokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;
    private static final Logger LOGGER = Logger.getLogger(BuyPokeLabController.class.getName());

    @FXML private Label totalPriceLabel;
    @FXML private Label caloriesLabel;
    @FXML private Pane popup;

     private static boolean retrieve = false; // se è false è perchè non ho poke da recuperare


    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {

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
            caloriesLabel.setText("CAL = " + pokeLabBean.getCalories() + " cal");

            GenericAlternative genericAlternative;

            genericAlternative = pokeLabBean.getIngredient("rice");
            if(genericAlternative != null){
                riceButton.setText(((RiceAlternative)genericAlternative).name());
            }

            genericAlternative = pokeLabBean.getIngredient("protein");
            if(genericAlternative != null){
                proteinButton.setText(((ProteinAlternative)genericAlternative).name());
            }

            genericAlternative = pokeLabBean.getIngredient("fruit");
            if(genericAlternative != null){
                fruitButton.setText(((FruitAlternative)genericAlternative).name());
            }

            genericAlternative = pokeLabBean.getIngredient("crunchy");
            if(genericAlternative != null){
                crunchyButton.setText(((CrunchyAlternative)genericAlternative).name());
            }

            genericAlternative = pokeLabBean.getIngredient("sauces");
            if(genericAlternative != null){
                saucesButton.setText(((SaucesAlternative)genericAlternative).name());
            }
        }

        checkIngredientSelection();
        checkBowlSizeSelection();

        if (retrieve){
            showPopUp();
            boolean value = false;
            setRetrieve(value);
        }

        updateSizeLabel();
    }

    public static void setRetrieve (boolean value) {retrieve = value;}

    //metodo per controllare se tutti gli ingredienti sono stati selezionati e in caso attiva il bottone della bowl size
    private void checkIngredientSelection(){
        bowlSizeButton.setDisable(!pokeLabBean.isComplete()); // Disabilita se manca almeno un ingrediente

    }

    private void checkBowlSizeSelection() {
        // Disabilita il pulsante se la bowl size non è selezionata
        addNameButton.setDisable(!pokeLabBean.isBowlSizeSelected());
    }


    @FXML
    public void handleRice(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addRice.fxml", pokeLabBean, id);
        checkIngredientSelection();
    }

    @FXML
    public void handleProtein(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addProtein.fxml", pokeLabBean, id);
        checkIngredientSelection();
    }

    @FXML
    public void handleFruit(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addFruit.fxml", pokeLabBean, id);
        checkIngredientSelection();
    }

    @FXML
    public void handleCrunchy(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addCrunchy.fxml", pokeLabBean, id);
        checkIngredientSelection();
    }

    @FXML
    public void handleSauces(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addSauces.fxml", pokeLabBean, id);
        checkIngredientSelection();
    }

    @FXML
    public void handleBowlSizeClick(ActionEvent event){
        if (!bowlSizeButton.isDisabled()){ // Controlla se il pulsante è attivo prima di cambiare scena
            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/bowlSize.fxml", pokeLabBean, id);
        }
    }

    private void updateSizeLabel() {
        if (sizeLabel != null && pokeLabBean.isBowlSizeSelected()) {
            sizeLabel.setText("Bowl Size: " + pokeLabBean.getBowlSize());
        }
    }


    @FXML
    public void handleAddNameClick(ActionEvent event) {
        if (!addNameButton.isDisabled()) { // Controlla se il pulsante è attivo prima di cambiare scena
            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addName.fxml", pokeLabBean, id);
        }
    }

    @FXML
    public void handleBackClick(ActionEvent event) throws SystemException{
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/homePage.fxml", pokeLabBean, id);
    }

    @FXML
    public void showPopUp() {popup.setVisible(true);}


    @FXML
    private void handleRetrieve() throws SystemException {
        popup.setVisible(false);
        PokeLabBean oldPokeLabBean = pokeLabAppController.retrievePokeLabBySession(id);
        if (oldPokeLabBean != null) {
            try {
                init(id, oldPokeLabBean);
            } catch (IOException | LoginException | SQLException e) {
                throw new SystemException("Error while retrieving PokèLab: " + e.getMessage());
            }
        } else {
            LOGGER.warning("Pokè Lab not found");
        }
    }



    @FXML
    private void handleNewPoke() {
        popup.setVisible(false);
    }
}
