package org.example.ispwproject.control.graphic.buyPokeLab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.graphic.GraphicController;

public class AddNameController  {

    @FXML
    private TextField pokeNameField;

    @FXML
    private Label errorLabel;

    @FXML
    public void handleExtra(ActionEvent event) {
        String name = pokeNameField.getText().trim();

        if (name.length() < 4) {
            errorLabel.setText("The name must be at least 4 characters long!");
        } else {
            errorLabel.setText(""); // Rimuove eventuali messaggi di errore
            System.out.println("Poké name set to: " + name);
            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/extra.fxml");
        }
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml");
    }
}
