package org.example.ispwproject.control.graphic;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.example.ispwproject.ChangePage;

public class SidebarController {
    @FXML
    private Label userLabel;

    @FXML
    public void handleHomeCLick(MouseEvent event) {
        System.out.println("Vai alla home");
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/homePage.fxml");
    }


    @FXML
    public void handleLogInCLick(MouseEvent event) {
        System.out.println("Logging in...");
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/login.fxml");
    }


    @FXML
    public void handleBuyPokeLabCLick(MouseEvent event) {
        System.out.println("Crea il tuo pokè!");
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml");
    }

    @FXML
    public void handlePostOnPokeWallCLick(MouseEvent event) {
        System.out.println("Posta su pokè wall");
    }


}