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
import org.example.ispwproject.utils.bean.PokeWallBean;
import org.example.ispwproject.utils.bean.SaveBean;
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

        // 1. Recupero la sessione corrente
        SessionManager sessionManager = SessionManager.getSessionManager();
        Session session = sessionManager.getSessionFromId(id);
        String userId = session.getUserId();

        try {
            // 2. Invoco il metodo di business che valida e salva il nome
            boolean ok = buyPokeLabAppController.setPokeName(
                    pokeLabBean,
                    name,
                    new SaveBean(userId)
            );

            // 3. Se tutto ok, cambio scena
            if (ok) {
                ChangePage.changeScene(
                        (Node) event.getSource(),
                        "/org/example/ispwproject/view/orderPokeLab.fxml",
                        pokeLabBean, id
                );
            }
        } catch (SystemException se) {
            // 4. Mostro l’errore in UI
            errorLabel.setText(se.getMessage());
        }
    }





    @FXML
    public void handleBackClick(ActionEvent event) throws SystemException{
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
    }


    @FXML
    public void handlePostToPokeWall(ActionEvent event) {
        // Ottieni l'istanza singleton di PokeWallAppController
        PokeWallAppController pokeWallAppController = PokeWallAppController.getInstance();

        String name = pokeNameField.getText().trim();

        // Controllo sul nome
        if (name.length() < 4) {
            errorLabel.setText("The name must be at least 4 characters long!");
            return;
        }

        // Recupera la sessione
        SessionManager sessionManager = SessionManager.getSessionManager();
        Session session = sessionManager.getSessionFromId(id);

        // Aggiungi il controllo se la sessione è null
        if (session == null) {
            errorLabel.setText("Session not found. Please log in again.");
            return;
        }

        String userId = session.getUserId();

        // Crea il PokeWallBean
        PokeWallBean pokeWallBean = new PokeWallBean();
        pokeWallBean.setPokeName(name);
        pokeWallBean.setSize(pokeLabBean.getBowlSize());
        pokeWallBean.setIngredients(
                pokeLabBean.getAllIngredients().entrySet().stream()
                        .map(entry -> entry.getKey() + ": " + entry.getValue().toString().toLowerCase())
                        .toList()
        );

        try {
            // Utilizza il metodo createPost tramite l'istanza singleton di PokeWallAppController
            boolean success = pokeWallAppController.createPost(
                    new SaveBean(userId),
                    pokeWallBean // Passa l'intero bean invece dei singoli campi
            );

            if (success) {
                ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/pokeWall.fxml", pokeLabBean, id);
            }
        } catch (SystemException _) {
            errorLabel.setText("Error during post creation");
        }
    }
}
