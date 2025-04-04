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
import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.control.application.PokeWallAppController;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.model.PokeWallObserver;
import org.example.ispwproject.model.observer.UserNotifier;
import org.example.ispwproject.model.observer.pokewall.PokeWall;
import org.example.ispwproject.model.user.User;
import org.example.ispwproject.model.user.UserDAO;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.dao.DAOFactory;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PokeWallController extends GraphicController implements PokeWallObserver {


    private PokeLabBean pokeLabBean;
    private int id;
    private String currentUsername;
    private List<PokeWall> currentPosts;
    private PokeWallAppController pokeWallAppController;
    private UserNotifier userNotifier;

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
        this.pokeWallAppController = new PokeWallAppController();

        pokeWallAppController.registerObserver(this);

        userNotifier = new UserNotifier();
        pokeWallAppController.registerObserver(userNotifier);

        SessionManager sessionManager = SessionManager.getSessionManager();
        Session session = sessionManager.getSessionFromId(id);
        UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
        User user = userDAO.read(session.getUserId());
        this.currentUsername = user.getUsername();

        this.pokeWallListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.deleteButton.setDisable(true);

        PokeWallAppController pokeWallAppC = new PokeWallAppController();
        List<PokeWall> posts = pokeWallAppC.getAllPosts();

        ObservableList<String> postObservableList = FXCollections.observableArrayList();

        for (PokeWall pokeWall : posts) {
            StringBuilder postText = new StringBuilder();
            postText.append(pokeWall.getUsername()).append(" created the Poke Lab: ").append(pokeWall.getPokeName()).append("\n");

            String pokeSize = pokeWall.getSize();
            if (pokeSize == null || pokeSize.isEmpty()) {
                pokeSize = "Unknown size"; // Default
            }

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


    @Override
    public void update(PokeWall newPost) {
        // Questo metodo viene chiamato quando viene aggiunto un nuovo post
        Platform.runLater(() -> {
            try {
                refreshPosts(); // Aggiorna la lista dei post

                // Mostra una notifica all'utente
                System.out.println("Nuovo post disponibile: " + newPost.getPokeName());
                // Potresti anche mostrare un alert o una notifica nell'UI
            } catch (SystemException e) {
                System.err.println("Error updating posts: " + e.getMessage());
            }
        });
    }

    private void refreshPosts() throws SystemException {
        currentPosts = pokeWallAppController.getAllPosts();

        ObservableList<String> postObservableList = FXCollections.observableArrayList();

        for (PokeWall pokeWall : currentPosts) {
            StringBuilder postText = new StringBuilder();
            postText.append(pokeWall.getUsername()).append(" created the Poke Lab: ").append(pokeWall.getPokeName()).append("\n");

            String pokeSize = pokeWall.getSize();
            if (pokeSize == null || pokeSize.isEmpty()) {
                pokeSize = "Unknown size";
            }

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
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/homePage.fxml", pokeLabBean, id);

    }

    @FXML
    public void handleDeletePost(ActionEvent event) {
        int selectedIndex = pokeWallListView.getSelectionModel().getSelectedIndex();

        if (isValidSelection(selectedIndex)) {
            PokeWall selectedPost = currentPosts.get(selectedIndex);

            if (canDeletePost(selectedPost)) {
                try {
                    deleteAndRefreshPosts(selectedPost);
                } catch (SystemException e) {
                    System.err.println("Error deleting post: " + e.getMessage());
                }
            }
        }
    }

    // Verifica se la selezione è valida
    private boolean isValidSelection(int selectedIndex) {
        return selectedIndex >= 0 && selectedIndex < currentPosts.size();
    }

    // Verifica se l'utente ha il permesso di eliminare il post
    private boolean canDeletePost(PokeWall selectedPost) {
        if (!selectedPost.getUsername().equals(currentUsername)) {
            System.out.println("You can only delete your own posts!");
            return false;
        }
        return true;
    }

    // Elimina il post, aggiorna la lista e ripristina la selezione
    private void deleteAndRefreshPosts(PokeWall selectedPost) throws SystemException {
        boolean success = pokeWallAppController.deletePost(selectedPost.getId(), currentUsername);

        if (success) {
            // Salva l'ID del post selezionato prima del refresh
            int selectedId = selectedPost.getId();

            // Esegui il refresh dei post
            refreshPosts();

            // Ripristina la selezione dopo il refresh
            restoreSelection(selectedId);

            System.out.println("Post deleted successfully!");
        } else {
            System.out.println("Failed to delete post.");
        }
    }

    // Ripristina la selezione del post dopo il refresh
    private void restoreSelection(int selectedId) {
        for (int i = 0; i < currentPosts.size(); i++) {
            if (currentPosts.get(i).getId() == selectedId) {
                pokeWallListView.getSelectionModel().select(i);
                break;
            }
        }
    }
}
