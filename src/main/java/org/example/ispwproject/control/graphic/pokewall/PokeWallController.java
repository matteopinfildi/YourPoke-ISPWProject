package  org.example.ispwproject.control.graphic.pokewall;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.event.ActionEvent;

import org.example.ispwproject.ChangePage;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.control.application.PokeWallAppController;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.model.PokeWallObserver;
import org.example.ispwproject.model.observer.pokewall.PokeWall;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PokeWallController extends GraphicController {

    private PokeLabBean pokeLabBean;
    private int id;
    private String currentUsername;
    private List<PokeWall> currentPosts;
    private PokeWallAppController pokeWallAppController;
    private PokeWallObserver observerInstance;
    private static final Logger logger = Logger.getLogger(PokeWallController.class.getName());

    @FXML private ListView<String> pokeWallListView;
    @FXML private Button deleteButton;
    @FXML private javafx.scene.control.Label notificationLabel;

    @Override
    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        this.pokeLabBean = pokeLabBean;
        this.id = id;

        pokeWallAppController = PokeWallAppController.getInstance();
        this.currentUsername = SessionManager.getSessionManager().getSessionFromId(id).getUserId();

        observerInstance = new PokeWallObserver() {
            @Override
            public void update(PokeWall newPost) {
                Platform.runLater(() -> {
                    try {
                        refreshPosts();
                        String message = "New post by " + newPost.getUsername() + ": " + newPost.getPokeName();
                        notificationLabel.setText(message);
                        notificationLabel.setVisible(true);
                        Thread hideThread = new Thread(() -> {
                            try {
                                Thread.sleep(5000);
                                Platform.runLater(() -> {
                                    notificationLabel.setVisible(false);
                                    notificationLabel.setText("");
                                });
                            } catch (InterruptedException e) {
                                throw new RuntimeException("Thread was interrupted during sleep", e);

                            }
                        });
                        hideThread.setDaemon(true);
                        hideThread.start();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("New post available");
                        alert.setHeaderText("A new poke lab has been created!");
                        alert.setContentText(message);
                        alert.showAndWait();
                    } catch (SystemException e) {
                        System.err.println("Error " + e.getMessage());
                    }
                });
            }
        };

        pokeWallAppController.registerObserver(observerInstance, id);

        pokeWallListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        deleteButton.setDisable(true);
        refreshPosts();

        pokeWallListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            deleteButton.setDisable(newVal == null);
        });
    }

    private void refreshPosts() throws SystemException {
        currentPosts = pokeWallAppController.getAllPosts();
        ObservableList<String> postObservableList = FXCollections.observableArrayList();

        for (PokeWall pokeWall : currentPosts) {
            StringBuilder postText = new StringBuilder();
            postText.append(pokeWall.getUsername()).append(" created the Poke Lab: ").append(pokeWall.getPokeName()).append("\n");

            String pokeSize = (pokeWall.getSize() == null || pokeWall.getSize().isEmpty()) ? "Unknown size" : pokeWall.getSize();
            postText.append("Poke size: ").append(pokeSize).append("\n");

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
    public void handleBackClick(ActionEvent event) throws SystemException {
        if (observerInstance != null) {
            pokeWallAppController.removeObserver(observerInstance);
        }

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

    private boolean isValidSelection(int selectedIndex) {
        return selectedIndex >= 0 && selectedIndex < currentPosts.size();
    }

    private boolean canDeletePost(PokeWall selectedPost) {
        if (!selectedPost.getUsername().equals(currentUsername)) {
            System.out.println("You can only delete your own posts!");
            return false;
        }
        return true;
    }

    private void deleteAndRefreshPosts(PokeWall selectedPost) throws SystemException {
        boolean success = pokeWallAppController.deletePost(selectedPost.getId(), currentUsername);

        if (success) {
            int selectedId = selectedPost.getId();
            refreshPosts();
            restoreSelection(selectedId);
            System.out.println("Post deleted successfully!");
        } else {
            System.out.println("Failed to delete post.");
        }
    }

    private void restoreSelection(int selectedId) {
        for (int i = 0; i < currentPosts.size(); i++) {
            if (currentPosts.get(i).getId() == selectedId) {
                pokeWallListView.getSelectionModel().select(i);
                break;
            }
        }
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }


    @FXML
    public void handleFortuneClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/fortuneCookie.fxml", pokeLabBean, id);
    }
}
