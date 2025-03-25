package org.example.ispwproject.control.graphic.pokewall;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PokeWallController extends GraphicController {

    private PokeLabBean pokeLabBean;
    private int id;
    // Aggiungi una ListView per visualizzare i post
    @FXML private ListView<String> pokeWallListView;

    // Questo metodo è usato per inizializzare i dati del controller
    @Override
    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        this.pokeLabBean = pokeLabBean;
        this.id = id;

        // Carica i post esistenti nella ListView
        List<String> posts = pokeLabBean.getPosts();
        if (posts != null && !posts.isEmpty()) {
            pokeWallListView.getItems().setAll(posts);
        } else {
            System.out.println("No post available!!!");
        }
    }

    // Altri metodi per interagire con i dati dei post (aggiungere, rimuovere post, etc.)

    @FXML
    public void handleBackClick (ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addName.fxml", pokeLabBean, id);

    }
}
