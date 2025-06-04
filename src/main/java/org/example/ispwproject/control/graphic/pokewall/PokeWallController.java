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
import org.example.ispwproject.control.PokeWallObserver;
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

        // ottengo l'istanza singleton del controller applicativo
        pokeWallAppController = PokeWallAppController.getInstance();
        // recuperiamo quale utente è loggato
        this.currentUsername = SessionManager.getSessionManager().getSessionFromId(id).getUserId();


        // implamentazione anonima di PokeWallObserver
        observerInstance = new PokeWallObserver() {
            @Override
            public void update(PokeWall newPost) {
                Platform.runLater(() -> {
                    try {
                        // recupero tutti i post e li ricarico
                        refreshPosts();
                        // viene costruita una notifica testuale
                        String message = generateNotificationMessage(newPost);
                        // viene mostrata la label della notifica
                        notificationLabel.setText(message);
                        notificationLabel.setVisible(true);
                        Thread hideThread = new Thread(() -> {
                            try {
                                Thread.sleep(5000);
                                Platform.runLater(() -> {
                                    notificationLabel.setVisible(false);
                                    notificationLabel.setText("");
                                });
                            } catch (InterruptedException _) {
                                logger.log(Level.SEVERE, "Thread was interrupted during sleep");
                            }
                        });
                        hideThread.setDaemon(true);
                        hideThread.start();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("New post available");
                        alert.setHeaderText("A new poke lab has been created!");
                        alert.setContentText(message);
                        alert.showAndWait();
                    } catch (SystemException _) {
                            logger.log(Level.SEVERE, "Error");
                    }
                });
            }
        };

        // registra l’istanza di PokeWallObserver appena creata presso il controller applicativo
        pokeWallAppController.registerObserver(observerInstance, id);

        // imposta la ListView in modo che l’utente possa selezionare al massimo un solo elemento per volta (utile per gestire l'eliminazione dei post)
        pokeWallListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        deleteButton.setDisable(true);
        refreshPosts();

        pokeWallListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            deleteButton.setDisable(newVal == null);
        });
    }

    // metodo che recupera tutti i post con i testi formattati in modo corretto
    private void refreshPosts() throws SystemException {
        // viene chiamato il metodo sul controller applicativo per ottenere tutti i post presenti sul poke wall
        currentPosts = pokeWallAppController.getAllPosts();
        // lista di stringhe visualizzabili
        ObservableList<String> postObservableList = FXCollections.observableArrayList();

        // scorriamo tutti i post presenti
        for (PokeWall pokeWall : currentPosts) {
            // per ogni post vengono trasformati i campi del post in un unica stringa strutturata
            String formattedPost = formatPostForDisplay(pokeWall);
            // aggiunge il post formattato alla lista
            postObservableList.add(formattedPost);
        }

        // vengono mostrati esattamente gli elementi presenti nella lista di post visualizzabili
        pokeWallListView.setItems(postObservableList);

    }

    @FXML
    public void handleBackClick(ActionEvent event) throws SystemException {
        // controllo che ci sia effettivamente un istanza di PokeWallObserver registrata
        if (observerInstance != null) {
            // rimuovo l'istanza dalla lista degli observer
            pokeWallAppController.removeObserver(observerInstance);
        }

        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/homePage.fxml", pokeLabBean, id);
    }

    @FXML
    public void handleDeletePost(ActionEvent event) {
        // recupera l'indice del post selezionato all'interno della lista
        int selectedIndex = pokeWallListView.getSelectionModel().getSelectedIndex();

        // chiama il metodo per controllare se l'indice è valido
        if (isValidSelection(selectedIndex)) {
            // viene estratta dalla lista currentPosts l'oggetto PokeWall (quindi il post) corrispondente all'indice selezionato
            PokeWall selectedPost = currentPosts.get(selectedIndex);

            try {
                // utilizza il metodo dell controller applicativo per verificare se l'utente può eliminare il post
                boolean canDelete = pokeWallAppController.canUserDeletePost(currentUsername, selectedPost.getId());

                if (canDelete) {
                    // se lo può eliminare, allora il post viene eliminato, sfruttando il metodo sul controller applicativo
                    boolean success = pokeWallAppController.deletePost(selectedPost.getId(), currentUsername);

                    if (success) {
                        // se l'eliminazione è andata a buon fine, viene riaggiornata la ListView che non mostrerà più il post eliminato
                        refreshPosts();
                        // alert per avvisare che è stato eliminato un post
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Post eliminated");
                        alert.setHeaderText("Post eliminated");
                        alert.setContentText("The post has been successfully deleted.");
                        alert.showAndWait();
                    } else {
                        logger.warning("Failed to delete post.");
                    }
                } else {
                    // alert per avvisare che non può eliminare un post che non è suo
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Operation Not Allowed");
                    alert.setHeaderText("Cannot Delete Post");
                    alert.setContentText("You do not have permission to delete this post.");
                    alert.showAndWait();
                }
            } catch (SystemException e) {
                logger.log(Level.SEVERE, "Error deleting post", e);
            }
        }
    }

    // viene verificato che l'indice passato sia compreso tra 0 e la lunghezza della lista dei post meno 1
    private boolean isValidSelection(int selectedIndex) {
        return selectedIndex >= 0 && selectedIndex < currentPosts.size();
    }



    @FXML
    public void handleFortuneClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/fortuneCookie.fxml", pokeLabBean, id);
    }


    // metodo che definisce la rappresentazione testuale di un post
    public String formatPostForDisplay(PokeWall post) {
        // creo un oggetto StringBuilder vuoto e ci vado ad aggiungere tutti i vari "pezzi"
        StringBuilder postText = new StringBuilder();
        // aggiungo username e pokeName
        postText.append(post.getUsername())
                .append(" created the Poke Lab: ")
                .append(post.getPokeName())
                .append("\n");

        String pokeSize = (post.getSize() == null || post.getSize().isEmpty())
                ? "Unknown size"
                : post.getSize();
        // aggiungo la size
        postText.append("Poke size: ").append(pokeSize).append("\n");

        // aggiungo tutti gli ingredienti
        for (String ingredient : post.getIngredients()) {
            postText.append("- ").append(ingredient).append("\n");
        }

        // si converte il contenuto dello StringBuilder in una vera e propria stringa
        return postText.toString();
    }

    // metodo che definisce come deve essere rappresentata la notifica
    public String generateNotificationMessage(PokeWall newPost) {
        return "New post by " + newPost.getUsername() + ": " + newPost.getPokeName();
    }
}
