package org.example.ispwproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.ChangePageException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChangePage {

    private static final Logger logger = Logger.getLogger(ChangePage.class.getName());
    private static ChangePage instance = null;

    protected ChangePage() {}

    public static synchronized ChangePage getChangePage(){
        if (ChangePage.instance == null) {
            ChangePage.instance = new ChangePage();
        }
        return instance;
    }

    private Stage stage;

    public static void changeScene(Node node, String fxmlPath, PokeLabBean pokeLabBean, int id) {
        try {
            FXMLLoader loader = new FXMLLoader(ChangePage.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            GraphicController controller = loader.getController();
            initController(controller, id, pokeLabBean);  // Initialize controller
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading FXML: " + e.getMessage(), e);  // Log error message
            e.printStackTrace();
        } catch (ChangePageException e) {
            logger.log(Level.SEVERE,"Error initializing controller: " + e.getMessage(), e);  // Log error message
            e.printStackTrace();
        }
    }

    // Handle controller initialization with error handling
    private static void initController(GraphicController controller, int id, PokeLabBean pokeLabBean) throws ChangePageException {
        try {
            controller.init(id, pokeLabBean);
        } catch (org.example.ispwproject.utils.exception.SystemException e) {
            throw new ChangePageException("System exception occurred while initializing controller", e);
        } catch (java.io.IOException e) {
            throw new ChangePageException("IO exception occurred while initializing controller", e);
        } catch (javax.security.auth.login.LoginException e) {
            throw new ChangePageException("Login exception occurred while initializing controller", e);
        } catch (java.sql.SQLException e) {
            throw new ChangePageException("SQL exception occurred while initializing controller", e);
        }
    }

    public void setStage(Stage stageParam){
        this.stage=stageParam;
    }

    public Stage getStage(){
        return this.stage;
    }
}
