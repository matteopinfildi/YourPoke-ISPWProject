package org.example.ispwproject.control.graphic.buypokelab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.ExtraBean;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.enumeration.SpicyType;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class ExtraController extends GraphicController {

    @FXML
    private ImageView noSelImage;
    @FXML
    private ImageView edemameImage;
    @FXML
    private ImageView cucumberImage;
    @FXML
    private ImageView carrotImage;
    @FXML
    private ImageView spicyImage;
    @FXML
    private ImageView chiliImage;
    @FXML
    private ImageView harissaImage;
    @FXML
    private ImageView hebaneroImage;

    @FXML
    private ToggleGroup emptyGroup;
    @FXML
    private RadioButton edemameRadio;
    @FXML
    private RadioButton cucumberRadio;
    @FXML
    private RadioButton carrotRadio;
    @FXML
    private RadioButton emptyRadio;

    @FXML
    private CheckBox edemameCrispyBox;
    @FXML
    private CheckBox cucumberCrispyBox;
    @FXML
    private CheckBox carrotCrispyBox;

    @FXML
    private Label extraPrice;

    @FXML
    private ComboBox comboBoxSpicySauce;
    @FXML
    private ComboBox comboBoxChili;
    @FXML
    private ComboBox comboBoxHarissa;
    @FXML
    private ComboBox comboBoxHebanero;

    @FXML private Label pokeNameLabel;

    private BuyPokeLabAppController buyPokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;


    public void initialize() {
        Image noSel = new Image(getClass().getResource("/org/example/ispwproject/image/noSelection.png").toExternalForm());
        noSelImage.setImage(noSel);

        Image edemame = new Image(getClass().getResource("/org/example/ispwproject/image/edemame.jpg").toExternalForm());
        edemameImage.setImage(edemame);

        Image cucumber = new Image(getClass().getResource("/org/example/ispwproject/image/cucumber.jpeg").toExternalForm());
        cucumberImage.setImage(cucumber);

        Image carrot = new Image(getClass().getResource("/org/example/ispwproject/image/carrot.jpg").toExternalForm());
        carrotImage.setImage(carrot);

        Image spicyIm = new Image(getClass().getResource("/org/example/ispwproject/image/spicySauce.jpg").toExternalForm());
        spicyImage.setImage(spicyIm);

        Image chili = new Image(getClass().getResource("/org/example/ispwproject/image/chili.jpg").toExternalForm());
        chiliImage.setImage(chili);

        Image harissa = new Image(getClass().getResource("/org/example/ispwproject/image/harissa.jpg").toExternalForm());
        harissaImage.setImage(harissa);

        Image hebanero = new Image(getClass().getResource("/org/example/ispwproject/image/hebanero.jpg").toExternalForm());
        hebaneroImage.setImage(hebanero);

        if (emptyRadio.isSelected()) {
            edemameCrispyBox.setDisable(true);
            cucumberCrispyBox.setDisable(true);
            carrotCrispyBox.setDisable(true);
        }

        emptyGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == emptyRadio) {
                // se "empty" è selezionato, deseleziona tutti gli altri RadioButton
                edemameRadio.setSelected(false);
                cucumberRadio.setSelected(false);
                carrotRadio.setSelected(false);

                edemameCrispyBox.setSelected(false);
                cucumberCrispyBox.setSelected(false);
                carrotCrispyBox.setSelected(false);
                // disabilita i CheckBox
                edemameCrispyBox.setDisable(true);
                cucumberCrispyBox.setDisable(true);
                carrotCrispyBox.setDisable(true);

                for (int i=0; i < toppings.length; i++){
                    updateTopping(i, false, false);
                }
            }
        });
    }

    @Override
    public void init(int sessionId, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = sessionId;

        String pokeName = pokeLabBean.getPokeName();
        if (pokeName != null && !pokeName.isEmpty()) {
            pokeNameLabel.setText("Add extra to " + pokeName + " pokè");
        } else {
            pokeNameLabel.setText("Add extra");
        }

        extraPrice.setText("Extra price: " + pokeLabBean.getPrice() + "$");

        for (SpicyType spicyType : SpicyType.values()) {
            updateSpicyType(spicyType.name(), 0);
        }

        comboBoxSpicySauce.setValue(0);
        comboBoxChili.setValue(0);
        comboBoxHarissa.setValue(0);
        comboBoxHebanero.setValue(0);
    }

    private boolean[][] toppings = new boolean[3][2];

    public void handleEdemame() {
        if (edemameRadio.isSelected()) {
            edemameCrispyBox.setDisable(false); // Abilita il CheckBox
            emptyRadio.setSelected(false); // Deseleziona "Vuoto" se un topping è selezionato
            updateTopping(0, true, false);
        } else {
            edemameCrispyBox.setDisable(true); // Disabilita il CheckBox
            edemameCrispyBox.setSelected(false); // Deseleziona il CheckBox
            updateTopping(0, false, false);
        }
    }

    public void handleCucumber() {
        if (cucumberRadio.isSelected()) {
            cucumberCrispyBox.setDisable(false); // Abilita il CheckBox
            emptyRadio.setSelected(false); // Deseleziona "Vuoto" se un topping è selezionato
            updateTopping(1, true, false);
        } else {
            cucumberCrispyBox.setDisable(true); // Disabilita il CheckBox
            cucumberCrispyBox.setSelected(false); // Deseleziona il CheckBox
            updateTopping(1, false, false);
        }
    }

    public void handleCarrot() {
        if (carrotRadio.isSelected()) {
            carrotCrispyBox.setDisable(false); // Abilita il CheckBox
            emptyRadio.setSelected(false); // Deseleziona "Vuoto" se un topping è selezionato
            updateTopping(2, true, false);
        } else {
            carrotCrispyBox.setDisable(true); // Disabilita il CheckBox
            carrotCrispyBox.setSelected(false); // Deseleziona il CheckBox
            updateTopping(2, false, false);
        }
    }

    public void handleEdemameCrispy() {
        updateTopping(0, true, edemameCrispyBox.isSelected());
    }

    public void handleCucumberCrispy() {
        updateTopping(1, true, cucumberCrispyBox.isSelected());
    }

    public void handleCarrotCrispy() {
        updateTopping(2, true, carrotCrispyBox.isSelected());
    }


    public void updateTopping(int tid, boolean select, boolean crispy) {
        toppings[tid][0] = select;
        toppings[tid][1] = crispy;
    }

    private HashMap<String, Integer> spicy = new HashMap<String, Integer>();

    public void updateSpicyType(String name, int quantity) {
        spicy.put(name, quantity);
    }

    public void handleExtraPrice() {
        spicy.put(SpicyType.SPICYSAUCE.name(), (Integer) comboBoxSpicySauce.getValue());
        spicy.put(SpicyType.CHILI.name(), (Integer) comboBoxChili.getValue());
        spicy.put(SpicyType.HARISSA.name(), (Integer) comboBoxHarissa.getValue());
        spicy.put(SpicyType.HEBANERO.name(), (Integer) comboBoxHebanero.getValue());

        ExtraBean extraBean = new ExtraBean( toppings, spicy);
        double newPrice = buyPokeLabAppController.putDecoration(pokeLabBean, extraBean);
        extraPrice.setText("Extra price: " + newPrice + "$");
    }


    @FXML
    public void handleNextClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/orderPokeLab.fxml", pokeLabBean, id);
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addName.fxml", pokeLabBean, id);
    }

    @FXML
    public void handleFortuneClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/fortuneCookie.fxml", pokeLabBean, id);
    }
}
