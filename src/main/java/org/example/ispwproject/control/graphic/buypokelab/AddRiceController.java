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
import org.example.ispwproject.utils.enumeration.ingredient.RiceOption;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class AddRiceController extends GraphicController {

    @FXML
    private ImageView sushiRiceImage;

    @FXML
    private ImageView venusRiceImage;

    @FXML
    private ImageView basmatiRiceImage;

    private BuyPokeLabAppController pokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;

    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException    {
        Image sushiRice = new Image(getClass().getResource("/org/example/ispwproject/image/sushiRice.jpeg").toExternalForm());
        sushiRiceImage.setImage(sushiRice);

        Image venusRice = new Image(getClass().getResource("/org/example/ispwproject/image/venusRice.jpg").toExternalForm());
        venusRiceImage.setImage(venusRice);

        Image basmatiRice = new Image(getClass().getResource("/org/example/ispwproject/image/basmatiRice.jpeg").toExternalForm());
        basmatiRiceImage.setImage(basmatiRice);

        pokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = id;
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

    @FXML private CheckBox checkSushi;

    @FXML private CheckBox checkVenus;

    @FXML private CheckBox checkBasmati;


    @FXML
    public void handleNextClick(ActionEvent event) throws SystemException {
        try {
            RiceOption riceOption = null;
            if (checkSushi.isSelected()) {
                riceOption = RiceOption.SUSHI;
            } else if (checkVenus.isSelected()) {
                riceOption = RiceOption.VENUS;
            } else if (checkBasmati.isSelected()) {
                riceOption = RiceOption.BASMATI;
            }

            AddIngredientBean addIngredientBean = new AddIngredientBean("rice", riceOption);
            pokeLabAppController.addIngredient(pokeLabBean, addIngredientBean);
            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);

        } catch (Exception e) {
            throw new SystemException("Error while selecting rice ingredient" + e.getMessage());
        }
    }


    @FXML
    public void handleBackClick(ActionEvent event) throws SystemException {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
    }
}
