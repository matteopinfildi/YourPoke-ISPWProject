package org.example.ispwproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.SystemException;

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
            initController(controller, id, pokeLabBean);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading FXML", e);
        } catch (SystemException e) {
            logger.log(Level.SEVERE, "Error initializing controller", e);
        }
    }


    private static void initController(GraphicController controller, int id, PokeLabBean pokeLabBean) throws SystemException {
        try {
            controller.init(id, pokeLabBean);
        } catch (IOException e) {
            throw new SystemException("IOException during controller initialization: " + e.getMessage());
        } catch (javax.security.auth.login.LoginException e) {
            throw new SystemException("Login error during controller initialization: " + e.getMessage());
        } catch (java.sql.SQLException e) {
            throw new SystemException("Database error during controller initialization: " + e.getMessage());
        }
    }



    public void setStage(Stage stageParam){
        this.stage=stageParam;
    }

    public Stage getStage(){
        return this.stage;
    }
}
