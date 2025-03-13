package org.example.ispwproject.control.graphic.buyPokeLab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import org.example.ispwproject.ChangePage;

public class ExtraController { @FXML

public void handleNextClick(ActionEvent event) {
    ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/extraIngredient.fxml");
}
public void handleBackClick(ActionEvent event) {
    ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addName.fxml");
}
}
