package org.example.ispwproject.control.graphic.buypokelab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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

    @FXML private ImageView noSelImage;
    @FXML private ImageView edemameImage;
    @FXML private ImageView cucumberImage;
    @FXML private ImageView carrotImage;
    @FXML private ImageView spicyImage;
    @FXML private ImageView chiliImage;
    @FXML private ImageView harissaImage;
    @FXML private ImageView hebaneroImage;

    @FXML private ToggleGroup emptyGroup;
    @FXML private RadioButton edemameRadio;
    @FXML private RadioButton cucumberRadio;
    @FXML private RadioButton carrotRadio;
    @FXML private RadioButton emptyRadio;

    @FXML private Label extraPrice;

    @FXML private ComboBox comboBoxSpicySauce;
    @FXML private ComboBox comboBoxChili;
    @FXML private ComboBox comboBoxHarissa;
    @FXML private ComboBox comboBoxHebanero;

    private BuyPokeLabAppController buyPokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;



    @Override
    public void initialize(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        Image noSel = new Image(getClass().getResource("/org/example/ispwproject/image/noSelection.png").toExternalForm());
        noSelImage.setImage(noSel);

        Image edemame = new Image(getClass().getResource("/org/example/ispwproject/image/edemame.jpg").toExternalForm());
        edemameImage.setImage(edemame);

        Image cucumber = new Image(getClass().getResource("/org/example/ispwproject/image/cucumber.jpeg").toExternalForm());
        cucumberImage.setImage(cucumber);

        Image carrot = new Image(getClass().getResource("/org/example/ispwproject/image/carrot.jpg").toExternalForm());
        carrotImage.setImage(carrot);

        Image spicy = new Image(getClass().getResource("/org/example/ispwproject/image/spicySauce.jpg").toExternalForm());
        spicyImage.setImage(spicy);

        Image chili = new Image(getClass().getResource("/org/example/ispwproject/image/chili.jpg").toExternalForm());
        chiliImage.setImage(chili);

        Image harissa = new Image(getClass().getResource("/org/example/ispwproject/image/harissa.jpg").toExternalForm());
        harissaImage.setImage(harissa);

        Image hebanero = new Image(getClass().getResource("/org/example/ispwproject/image/hebanero.jpg").toExternalForm());
        hebaneroImage.setImage(hebanero);

        emptyGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == emptyRadio) {
            // se "empty" è selezionato, deseleziona tutti gli altri RadioButton
                edemameRadio.setSelected(false);
                cucumberRadio.setSelected(false);
                carrotRadio.setSelected(false);
            }
        });
    }

    @Override
    public void init(int sessionId, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException{
        buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = id;

        extraPrice.setText("Extra price: " + pokeLabBean.getPrice() + "$");

        for (SpicyType spicyType : SpicyType.values()){
            updateSpicyType(spicyType.name(), 0);
        }

        comboBoxSpicySauce.setValue(0);
        comboBoxChili.setValue(0);
        comboBoxHarissa.setValue(0);
        comboBoxHebanero.setValue(0);
    }

    private HashMap<String, Integer> spicy = new HashMap<String, Integer>();

    public void updateSpicyType(String name, int quantity) { spicy.put(name, quantity);}

    public void handleCalculate(ActionEvent event){
        spicy.put(SpicyType.SPICYSAUCE.name(), (Integer) comboBoxSpicySauce.getValue());
        spicy.put(SpicyType.CHILI.name(), (Integer) comboBoxChili.getValue());
        spicy.put(SpicyType.HARISSA.name(), (Integer) comboBoxHarissa.getValue());
        spicy.put(SpicyType.HEBANERO.name(), (Integer) comboBoxHebanero.getValue());

        //aggiungere i controlli per il crispy e in teoria ho finito

//        ExtraBean extraBean = new ExtraBean(spicy, toppings);
//        double extraPrice = buyPokeLabAppController.addIngredient();
    }

    @FXML
    public void handleNextClick(ActionEvent event) {
        System.out.println("Fattooo!");
    }
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addName.fxml", pokeLabBean, id);
    }
}
