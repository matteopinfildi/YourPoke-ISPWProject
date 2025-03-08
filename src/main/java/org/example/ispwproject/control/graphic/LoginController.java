package org.example.ispwproject.control.graphic;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import org.example.ispwproject.ChangePage;

public class LoginController {
    public void handleLogin(ActionEvent event) {

    }

    public void handleBackToHome(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/homePage.fxml");
    }
}
