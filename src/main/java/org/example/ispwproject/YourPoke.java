package org.example.ispwproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.ispwproject.cli.CliHomePage;
import org.example.ispwproject.cli.CliLogin;
import org.example.ispwproject.control.graphic.LoginController;
import org.example.ispwproject.utils.enumeration.UserType;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

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

    public static void main(String[] args) throws SystemException, IOException, LoginException, SQLException {

        SetUp setUp = new SetUp();

        if (!setUp.setUpPersistenceProvider()) {
            System.exit(0);
        }

        switch (setUp.setUI()) {
            case GUI:
                LoginController loginController = new LoginController();
                loginController.registerUser("matteoP", "ciao123", "matteoP@gmail.com", UserType.USER, "via Domenico Modugno 8");
                loginController.registerUser("marcoB", "hola123", "marcoB@gmail.com", UserType.PREMIUMUSER, "via Massimo Troisi 11");

                launch();
                break;

            case CLI:
                CliLogin cliLogin = new CliLogin();
                cliLogin.register("matteoP", "ciao123", "matteoP@gmail.com", UserType.USER, "via Domenico Modugno 8");
                cliLogin.register("marcoB", "hola123", "marcoB@gmail.com", UserType.PREMIUMUSER, "via Massimo Troisi 11");

                new CliHomePage().init(-1, null);
                break;
        }
    }
}
