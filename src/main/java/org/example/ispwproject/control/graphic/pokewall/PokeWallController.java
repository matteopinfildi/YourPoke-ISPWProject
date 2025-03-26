package org.example.ispwproject.control.graphic.pokewall;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
    private List<PokeWall> currentPosts;
    private PokeWallAppController pokeWallAppController;
    // ListView per visualizzare i post
    @FXML private ListView<String> pokeWallListView;
    @FXML private Button deleteButton;


    // Questo metodo è usato per inizializzare i dati del controller
    @Override
    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        this.pokeLabBean = pokeLabBean;
        this.id = id;
        this.pokeWallListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.deleteButton.setDisable(true);
        pokeWallAppController = new PokeWallAppController();

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

        // Listener per abilitare il bottone quando un post è selezionato
        pokeWallListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            deleteButton.setDisable(newVal == null);
        });

        refreshPosts();
    }

    private void refreshPosts() throws SystemException {
        PokeWallAppController pokeWallAppController = new PokeWallAppController();
        currentPosts = pokeWallAppController.getAllPosts();

        ObservableList<String> postObservableList = FXCollections.observableArrayList();

        for (PokeWall pokeWall : currentPosts) {
            StringBuilder postText = new StringBuilder();
            postText.append(pokeWall.getUsername()).append(" created the Poke Lab: ").append(pokeWall.getPokeName()).append("\n");
            postText.append("Poke size: ").append(pokeWall.getSize()).append("\n");

            for (String ingredient : pokeWall.getIngredients()) {
                postText.append("- ").append(ingredient).append("\n");
            }

            postObservableList.add(postText.toString());
        }

        pokeWallListView.setItems(postObservableList);
    }

    @FXML
    public void handleBackClick (ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addName.fxml", pokeLabBean, id);

    }

    @FXML
    public void handleDeletePost(ActionEvent event) {
        int selectedIndex = pokeWallListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0 && selectedIndex < currentPosts.size()) {
            try {
                // Passiamo l'indice invece dell'ID
                boolean success = pokeWallAppController.deletePost(selectedIndex);
                if (success) {
                    refreshPosts();
                    System.out.println("Post deleted successfully!");
                } else {
                    System.out.println("Failed to delete post.");
                }
            } catch (SystemException e) {
                System.err.println("Error deleting post: " + e.getMessage());
            }
        }
    }
}
