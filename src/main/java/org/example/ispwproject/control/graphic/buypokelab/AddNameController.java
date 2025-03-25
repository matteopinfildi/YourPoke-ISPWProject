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
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.enumeration.ingredient.GenericAlternative;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        if (pokeLabBean.getPokeName() != null) {
            pokeNameField.setText(pokeLabBean.getPokeName());
        }
    }


    @FXML
    public void handleNextClick(ActionEvent event) {
        String name = pokeNameField.getText().trim();

        SessionManager sessionManager = SessionManager.getSessionManager();
        Session session = sessionManager.getSessionFromId(id);
        String userId = session.getUserId();

        if (name.length() < 4) {
            errorLabel.setText("The name must be at least 4 characters long!");
        } else {
            // Se il nome è lo stesso di prima, non serve salvare di nuovo
            if (name.equals(pokeLabBean.getPokeName())) {
                ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/extra.fxml", pokeLabBean, id);
                return;
            }
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

    @FXML
    public void handlePostToPokeWall(ActionEvent event) {
        String name = pokeNameField.getText().trim();

        if (name.length() < 4) {
            errorLabel.setText("The name must be at least 4 characters long!");
            return;
        }

        // Recupera l'utente dalla sessione
        SessionManager sessionManager = SessionManager.getSessionManager();
        Session session = sessionManager.getSessionFromId(id);
        String username = session.getUserId();

        pokeLabBean.setPokeName(name);

        // Costruisci il contenuto del post
        StringBuilder postContent = new StringBuilder();
        postContent.append(username).append(" created a new PokéLab: ").append(name).append("\n");
        postContent.append("Bowl Size: ").append(pokeLabBean.getBowlSize()).append("\n");

        List<String> ingredientList = new ArrayList<>();
        for (Map.Entry<String, GenericAlternative> entry : pokeLabBean.getAllIngredients().entrySet()) {
            String ingredientLine = entry.getKey() + ": " + entry.getValue();
            ingredientList.add(ingredientLine);
            postContent.append("- ").append(ingredientLine).append("\n");
        }

        postContent.append("Total Price: $").append(pokeLabBean.getPrice());

        // Aggiungi il post alla lista
        pokeLabBean.addPost(postContent.toString());

        PokeWallAppController pokeWallAppController = new PokeWallAppController();
        try {
            pokeWallAppController.createPost(new SaveBean(session.getUserId()), postContent.toString(), ingredientList);
            System.out.println("PokéLab posted to Poke Wall!");
            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/pokeWall.fxml", pokeLabBean, id);
        } catch (SystemException e) {
            errorLabel.setText("Failed to post PokéLab to Poke Wall.");
        }
    }

}
