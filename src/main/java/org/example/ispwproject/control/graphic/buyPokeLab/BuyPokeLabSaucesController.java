package org.example.ispwproject.control.graphic.buyPokeLab;

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
import org.example.ispwproject.utils.enumeration.ingredient.SaucesAlternative;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class BuyPokeLabSaucesController extends GraphicController {
    @FXML
    private ImageView teriyakiImage;

    @FXML
    private ImageView soyImage;

    @FXML
    private ImageView wasabiImage;

    private BuyPokeLabAppController pokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;

    public void initialize(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        Image teriyaki = new Image(getClass().getResource("/org/example/ispwproject/image/teriyaki.jpg").toExternalForm());
        teriyakiImage.setImage(teriyaki);

        Image soy = new Image(getClass().getResource("/org/example/ispwproject/image/soy.jpg").toExternalForm());
        soyImage.setImage(soy);

        Image wasabi = new Image(getClass().getResource("/org/example/ispwproject/image/wasabi.jpg").toExternalForm());
        wasabiImage.setImage(wasabi);
    }


    @FXML
    private VBox teriyakiNutritionalValues;

    @FXML
    private VBox soyNutritionalValues;

    @FXML
    private VBox wasabiNutritionalValues;

    @FXML
    private void toggleTeriyakiNutritional() {
        teriyakiNutritionalValues.setVisible(!teriyakiNutritionalValues.isVisible());
    }

    @FXML
    private void toggleSoyNutritional() {
        soyNutritionalValues.setVisible(!soyNutritionalValues.isVisible());
    }

    @FXML
    private void toggleWasabiNutritional() {
        wasabiNutritionalValues.setVisible(!wasabiNutritionalValues.isVisible());
    }

    @FXML private CheckBox checkTeriyaki;

    @FXML private CheckBox checkSoy;

    @FXML private CheckBox checkWasabi;

    @FXML
    public void handleNextClick(ActionEvent event) {
        try{
            SaucesAlternative saucesAlternative = null;
            if (checkTeriyaki.isSelected()) {
                saucesAlternative = SaucesAlternative.TERIYAKI;
            } else if (checkSoy.isSelected()) {
                saucesAlternative = SaucesAlternative.SOY;
            } else if (checkWasabi.isSelected()) {
                saucesAlternative = SaucesAlternative.WASABI;
            }

            AddIngredientBean addIngredientBean = new AddIngredientBean("sauces", saucesAlternative);
            pokeLabAppController.addIngredient(pokeLabBean, addIngredientBean);

            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
    }
}
