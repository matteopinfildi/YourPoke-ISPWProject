package org.example.ispwproject.control.graphic.buypokelab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class AddNameController extends GraphicController{

    @FXML
    private TextField pokeNameField;

    @FXML
    private Label errorLabel;

    private BuyPokeLabAppController buyPokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;

    @Override
    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = id;
    }


    @FXML
    public void handleExtra(ActionEvent event) {
        String name = pokeNameField.getText().trim();

        SessionManager sessionManager = SessionManager.getSessionManager();
        Session session = sessionManager.getSessionFromId(id);
        String userId = session.getUserId();

        if (name.length() < 4) {
            errorLabel.setText("The name must be at least 4 characters long!");
        } else {
            //associo il nome del poke alla rispettiva bean
            pokeLabBean.setPokeName(name);

            SaveBean saveBean = new SaveBean(userId);

            if (!buyPokeLabAppController.savePokeLab(pokeLabBean, saveBean)){
                System.out.println("Save failed");
                return;
            } else {
                System.out.println("Save successfull");
            }

            errorLabel.setText(""); // Rimuove eventuali messaggi di errore
            System.out.println("Poké name set to: " + name);
            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/extra.fxml", pokeLabBean, id);
        }
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
    }
}
