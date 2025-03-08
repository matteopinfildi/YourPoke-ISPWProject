package org.example.ispwproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class YourPoke extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Caricamento corretto del file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ispwproject/view/homePage.fxml"));
            Parent root = loader.load();

            // Caricamento del CSS
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/ispwproject/style.css").toExternalForm());

            primaryStage.getIcons().add(new Image("logoApp.PNG"));

            // Configura la finestra
            primaryStage.setTitle("YourPoké");
            primaryStage.setResizable(false); // rende la finestra non ridimensionabile
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
