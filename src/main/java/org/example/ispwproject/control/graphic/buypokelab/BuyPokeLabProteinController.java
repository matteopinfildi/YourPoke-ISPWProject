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
import org.example.ispwproject.utils.enumeration.ingredient.ProteinAlternative;
import org.example.ispwproject.utils.exception.PokeLabSystemException;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

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

    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        Image salmon = new Image(getClass().getResource("/org/example/ispwproject/image/salmon.jpg").toExternalForm());
        salmonImage.setImage(salmon);

        Image shrimp = new Image(getClass().getResource("/org/example/ispwproject/image/shrimp.jpg").toExternalForm());
        shrimpImage.setImage(shrimp);

        Image tuna = new Image(getClass().getResource("/org/example/ispwproject/image/tuna.jpg").toExternalForm());
        tunaImage.setImage(tuna);

        pokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = id;
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


    @FXML private CheckBox checkSalmon;

    @FXML private CheckBox checkShrimp;

    @FXML private CheckBox checkTuna;


    @FXML
    public void handleNextClick(ActionEvent event) throws PokeLabSystemException {
        try{
            ProteinAlternative proteinAlternative = null;
            if (checkSalmon.isSelected()) {
                proteinAlternative = ProteinAlternative.SALMON;
            } else if (checkShrimp.isSelected()) {
                proteinAlternative = ProteinAlternative.SHRIMP;
            } else if (checkTuna.isSelected()) {
                proteinAlternative = ProteinAlternative.TUNA;
            }

            AddIngredientBean addIngredientBean = new AddIngredientBean("protein", proteinAlternative);
            pokeLabAppController.addIngredient(pokeLabBean, addIngredientBean);

            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
        } catch (Exception e){
            throw new PokeLabSystemException("Error", e);
        }
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
    }
}
