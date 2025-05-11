package org.example.ispwproject.control.graphic.buypokelab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class OrderPokeLabController extends GraphicController {

    private int id;
    PokeLabBean pokeLabBean;

    @FXML private TextField addressField;
    @FXML private RadioButton cashRadioButton;
    @FXML private RadioButton cardRadioButton;
    @FXML private RadioButton paypalRadioButton;
    @FXML private TextArea notesArea;
    @FXML private Button confirmButton;

    @Override
    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        this.id = id;
        this.pokeLabBean = pokeLabBean;

        ToggleGroup paymentGroup = new ToggleGroup();
        cashRadioButton.setToggleGroup(paymentGroup);
        cardRadioButton.setToggleGroup(paymentGroup);
        paypalRadioButton.setToggleGroup(paymentGroup);
    }

    @FXML
    private void handleConfirmOrder() {
        String address = addressField.getText();

        // Controllo se l'indirizzo è stato inserito
        if (address.isEmpty()) {
            showAlert("Errore", "Inserisci un indirizzo valido.");
            return;
        }

        // Controllo se è stato selezionato un metodo di pagamento
        if (!cashRadioButton.isSelected() && !cardRadioButton.isSelected() && !paypalRadioButton.isSelected()) {
            showAlert("Errore", "Seleziona un metodo di pagamento.");
            return;
        }

        // Messaggio di conferma
        showAlert("Ordine Confermato", "Grazie per il tuo ordine!");
    }

    // Metodo per mostrare una finestra di avviso
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/addName.fxml", pokeLabBean, id);
    }
}
