package org.example.ispwproject.control.graphic.buypokelab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.AddIngredientBean;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.enumeration.ingredient.CrunchyAlternative;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class AddCrunchyController extends GraphicController{
    @FXML
    private ImageView onionImage;

    @FXML
    private ImageView nutsImage;

    @FXML
    private ImageView almondsImage;

    private BuyPokeLabAppController pokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;

    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        Image onion = new Image(getClass().getResource("/org/example/ispwproject/image/onion.jpg").toExternalForm());
        onionImage.setImage(onion);

        Image nuts = new Image(getClass().getResource("/org/example/ispwproject/image/nuts.jpg").toExternalForm());
        nutsImage.setImage(nuts);

        Image almonds = new Image(getClass().getResource("/org/example/ispwproject/image/almonds.jpg").toExternalForm());
        almondsImage.setImage(almonds);

        pokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = id;

    }


    @FXML
    private VBox onionNutritionalValues;

    @FXML
    private VBox nutsNutritionalValues;

    @FXML
    private VBox almondsNutritionalValues;

    @FXML
    private void toggleOnionNutritional() {
        onionNutritionalValues.setVisible(!onionNutritionalValues.isVisible());
    }

    @FXML
    private void toggleNutsNutritional() {
        nutsNutritionalValues.setVisible(!nutsNutritionalValues.isVisible());
    }

    @FXML
    private void toggleAlmondsNutritional() {
        almondsNutritionalValues.setVisible(!almondsNutritionalValues.isVisible());
    }

    @FXML private CheckBox checkOnion;

    @FXML private CheckBox checkNuts;

    @FXML private CheckBox checkAlmonds;

    @FXML
    public void handleNextClick(ActionEvent event) throws SystemException {
        try {
            CrunchyAlternative crunchyAlternative = null;
            if (checkOnion.isSelected()) {
                crunchyAlternative = CrunchyAlternative.ONION;
            } else if (checkNuts.isSelected()) {
                crunchyAlternative = CrunchyAlternative.NUTS;
            } else if (checkAlmonds.isSelected()) {
                crunchyAlternative = CrunchyAlternative.ALMONDS;
            }

            AddIngredientBean addIngredientBean = new AddIngredientBean("crunchy", crunchyAlternative);
            pokeLabAppController.addIngredient(pokeLabBean, addIngredientBean);
            
            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
        } catch (Exception e){
            throw new SystemException("Error" + e.getMessage());
        }
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
    }
}
