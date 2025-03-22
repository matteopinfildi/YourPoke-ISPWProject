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
import org.example.ispwproject.utils.enumeration.ingredient.FruitAlternative;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

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

    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
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

    @FXML private CheckBox checkAvocado;

    @FXML private CheckBox checkMango;

    @FXML private CheckBox checkStrawbarries;

    @FXML
    public void handleNextClick(ActionEvent event) {
        try{
            FruitAlternative fruitAlternative = null;
            if (checkAvocado.isSelected()) {
                fruitAlternative = FruitAlternative.AVOCADO;
            } else if (checkMango.isSelected()) {
                fruitAlternative = FruitAlternative.MANGO;
            } else if (checkStrawbarries.isSelected()) {
                fruitAlternative = FruitAlternative.STRAWBARRIES;
            }

            AddIngredientBean addIngredientBean = new AddIngredientBean("fruit", fruitAlternative);
            pokeLabAppController.addIngredient(pokeLabBean, addIngredientBean);

            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
    }
}
