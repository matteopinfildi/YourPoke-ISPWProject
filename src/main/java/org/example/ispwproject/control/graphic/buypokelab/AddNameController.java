package org.example.ispwproject.control.graphic.buypokelab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class AddNameController  extends GraphicController{

    @FXML
    private TextField pokeNameField;

    @FXML
    private Label errorLabel;

    private PokeLabBean pokeLabBean;
    private int id;

    @Override
    public void initialize(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {}


    @FXML
    public void handleExtra(ActionEvent event) {
        String name = pokeNameField.getText().trim();

        if (name.length() < 4) {
            errorLabel.setText("The name must be at least 4 characters long!");
        } else {
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
