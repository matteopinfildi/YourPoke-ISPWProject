package org.example.ispwproject.control.graphic.pokewall;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.application.PokeWallAppController;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.model.pokewall.PokeWall;
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
    // ListView per visualizzare i post
    @FXML private ListView<String> pokeWallListView;
    @FXML private Button deleteButton;

    // Questo metodo è usato per inizializzare i dati del controller
    @Override
    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        this.pokeLabBean = pokeLabBean;
        this.id = id;

        PokeWallAppController pokeWallAppController = new PokeWallAppController();
        List<PokeWall> posts = pokeWallAppController.getAllPosts();

        ObservableList<String> postObservableList = FXCollections.observableArrayList();

        for (PokeWall pokeWall : posts) {
            StringBuilder postText = new StringBuilder();
            postText.append(pokeWall.getUsername()).append(" created the Poke Lab: ").append(pokeWall.getPokeName()).append("\n");
            postText.append("Poke size: ").append(pokeWall.getSize()).append("\n");

            // Aggiungi gli ingredienti
            for (String ingredient : pokeWall.getIngredients()) {
                postText.append("- ").append(ingredient).append("\n");
            }

            postObservableList.add(postText.toString());
        }

        pokeWallListView.setItems(postObservableList);

        if (postObservableList.isEmpty()) {
            System.out.println("No posts available!");
        }
    }


    @FXML
    public void handleBackClick (ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addName.fxml", pokeLabBean, id);

    }
}
