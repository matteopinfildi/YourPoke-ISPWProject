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
import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.enumeration.ingredient.*;
import org.example.ispwproject.utils.exception.PokeLabSystemException;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

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

    @FXML private Label totalPriceLabel;
    @FXML private Pane popup;

     private static boolean recover = false; // se è false è perchè non ho poke da recuperare


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

        if (recover){
            showPopUp();
            boolean value = false;
            setRecover(value);
        }

        updateSizeLabel();
    }

    public static void setRecover (boolean value) {recover = value;}

    //metodo per controllare se tutti gli ingredienti sono stati selezionati
    private void checkIngredientSelection(){
        boolean allSelected = pokeLabBean.getIngredient("rice") != null &&
                pokeLabBean.getIngredient("protein") != null &&
                pokeLabBean.getIngredient("fruit") != null &&
                pokeLabBean.getIngredient("crunchy") != null &&
                pokeLabBean.getIngredient("sauces") != null;

        bowlSizeButton.setDisable(!allSelected); // Disabilita se manca almeno un ingrediente

    }

    private void checkBowlSizeSelection() {
        // Verifica se la dimensione della bowl è stata selezionata
        boolean sizeSelected = pokeLabBean.getBowlSize() != null && !pokeLabBean.getBowlSize().isEmpty();

        // Disabilita il pulsante se la bowl size non è selezionata
        addNameButton.setDisable(!sizeSelected);
    }


    @FXML
    public void handleRice(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabRice.fxml", pokeLabBean, id);
        checkIngredientSelection();
    }

    @FXML
    public void handleProtein(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabProtein.fxml", pokeLabBean, id);
        checkIngredientSelection();
    }

    @FXML
    public void handleFruit(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabFruit.fxml", pokeLabBean, id);
        checkIngredientSelection();
    }

    @FXML
    public void handleCrunchy(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabCrunchy.fxml", pokeLabBean, id);
        checkIngredientSelection();
    }

    @FXML
    public void handleSauces(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLabSauces.fxml", pokeLabBean, id);
        checkIngredientSelection();
    }

    @FXML
    public void handleBowlSizeClick(ActionEvent event) {
        if (!bowlSizeButton.isDisabled()){ // Controlla se il pulsante è attivo prima di cambiare scena
            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/bowlSize.fxml", pokeLabBean, id);
        }
    }

    private void updateSizeLabel() {
        if (sizeLabel != null && pokeLabBean.getBowlSize() != null) {
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
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/homePage.fxml", pokeLabBean, id);
    }

    @FXML
    public void showPopUp() {popup.setVisible(true);}

    @FXML
    private void handleRecover() throws PokeLabSystemException {
        popup.setVisible(false);

        SessionManager sessionManager = SessionManager.getSessionManager();
        Session session = sessionManager.getSessionFromId(id);
        String userId = session.getUserId();

        SaveBean saveBean = new SaveBean(userId);

        PokeLabBean oldPokeLabBean = pokeLabAppController.recoverPokeLab(saveBean);
        if (oldPokeLabBean != null) {
            try{
                init(id, oldPokeLabBean);
            } catch (SystemException e) {
                throw new PokeLabSystemException("Error", e);
            } catch (IOException e) {
                throw new PokeLabSystemException("Error", e);
            } catch (LoginException e) {
                throw new PokeLabSystemException("Error", e);
            } catch (SQLException throwables) {
                throw new PokeLabSystemException("Error", throwables);
            }
        } else {System.out.println("Pokè Lab not found");}
    }

    @FXML
    private void handleNewPoke() {
        popup.setVisible(false);
    }
}
