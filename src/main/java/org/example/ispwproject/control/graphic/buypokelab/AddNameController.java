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
import org.example.ispwproject.control.application.PokeWallAppController;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;


public class AddNameController extends GraphicController {

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

        if (pokeLabBean.getPokeName() != null) {
            pokeNameField.setText(pokeLabBean.getPokeName());
        }
    }



    @FXML
    public void handleNextClick(ActionEvent event) {
        String name = pokeNameField.getText().trim();

        SessionManager sessionManager = SessionManager.getSessionManager();
        Session session = sessionManager.getSessionFromId(id);
        try {
            boolean ok = buyPokeLabAppController.setPokeName(pokeLabBean, name, id);

            if (ok) {
                ChangePage.changeScene(
                        (Node) event.getSource(), "/org/example/ispwproject/view/orderPokeLab.fxml", pokeLabBean, id);
            }
        } catch (SystemException se) {
            errorLabel.setText(se.getMessage());
        }
    }



    @FXML
    public void handleBackClick(ActionEvent event) throws SystemException{
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
    }



    @FXML
    public void handlePostOnPokeWall(ActionEvent event) {
        String name = pokeNameField.getText().trim();
        PokeWallAppController pokeWallAppController = PokeWallAppController.getInstance();

        try {
            boolean success = pokeWallAppController.createPost(id, name, pokeLabBean);

            if (success) {
                ChangePage.changeScene(
                        (Node) event.getSource(), "/org/example/ispwproject/view/pokeWall.fxml", pokeLabBean, id);
            }
        } catch (SystemException se) {
            errorLabel.setText(se.getMessage());
        }
    }
}
