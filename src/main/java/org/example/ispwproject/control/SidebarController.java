package org.example.ispwproject.control;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class SidebarController {
    @FXML
    private Label userLabel;

    @FXML
    public void handleHomeCLick(MouseEvent event) {
        System.out.println("Vai alla home");
        // Logica per caricare la homepage
    }


    @FXML
    public void handleLogInCLick(MouseEvent event) {
        System.out.println("Logging out...");
    }


    @FXML
    public void handleBuyPokeLabCLick(MouseEvent event) {
        System.out.println("Crea il tuo pokè!");
    }

    @FXML
    public void handlePostOnPokeWallCLick(MouseEvent event) {
        System.out.println("Posta su pokè wall");
    }


}