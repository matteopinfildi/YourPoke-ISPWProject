package org.example.ispwproject.control.graphic.buypokelab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class ExtraController extends GraphicController {

    @FXML private ImageView noSelImage;
    @FXML private ImageView edemameImage;
    @FXML private ImageView cucumberImage;
    @FXML private ImageView carrotImage;
    @FXML private ImageView spicyImage;
    @FXML private ImageView chiliImage;
    @FXML private ImageView harissaImage;
    @FXML private ImageView hebaneroImage;

    private PokeLabBean pokeLabBean;
    private int id;

    // DA FINIRE!!!

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
    }

    @FXML
    public void handleNextClick(ActionEvent event) {
        System.out.println("Fattooo!");
    }
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addName.fxml", pokeLabBean, id);
    }
}
