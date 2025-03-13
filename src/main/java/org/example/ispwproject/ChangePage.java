package org.example.ispwproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;

import java.io.IOException;

public class ChangePage {

    private static ChangePage instance = null;

    protected ChangePage() {}

    public static ChangePage getChangePage(){
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
            try {
                controller.initialize(id, pokeLabBean);
            } catch (org.example.ispwproject.utils.exception.SystemException e) {
                throw new RuntimeException(e);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            } catch (javax.security.auth.login.LoginException e) {
                throw new RuntimeException(e);
            } catch (java.sql.SQLException throwables) {
                throw new RuntimeException(throwables);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setStage(Stage stageParam){
        this.stage=stageParam;
    }

    public Stage getStage(){
        return this.stage;
    }
}
