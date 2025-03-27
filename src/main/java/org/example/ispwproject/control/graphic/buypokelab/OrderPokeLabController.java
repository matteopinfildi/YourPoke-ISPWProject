package org.example.ispwproject.control.graphic.buypokelab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.utils.bean.PokeLabBean;

public class OrderPokeLabController {

    @FXML private TextField addressField;
    @FXML private RadioButton cashRadioButton;
    @FXML private RadioButton cardRadioButton;
    @FXML private TextArea notesArea;
    @FXML private Button confirmButton;

    @FXML
    public void initialize() {
        // Crea un ToggleGroup per i RadioButton in modo che solo uno possa essere selezionato
        ToggleGroup paymentGroup = new ToggleGroup();
        cashRadioButton.setToggleGroup(paymentGroup);
        cardRadioButton.setToggleGroup(paymentGroup);
    }

    @FXML
    private void handleConfirmOrder() {
        String address = addressField.getText();
        String paymentMethod = cashRadioButton.isSelected() ? "Pagamento alla consegna" : "Carta di credito";
        String notes = notesArea.getText();

        // Controllo se l'indirizzo è stato inserito
        if (address.isEmpty()) {
            showAlert("Errore", "Inserisci un indirizzo valido.");
            return;
        }

        // Controllo se è stato selezionato un metodo di pagamento
        if (!cashRadioButton.isSelected() && !cardRadioButton.isSelected()) {
            showAlert("Errore", "Seleziona un metodo di pagamento.");
            return;
        }

        // Simulazione della conferma dell'ordine
        System.out.println("Ordine Confermato!");
        System.out.println("Indirizzo: " + address);
        System.out.println("Metodo di Pagamento: " + paymentMethod);
        System.out.println("Note Extra: " + notes);

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

//    @FXML
//    public void handleBackClick(ActionEvent event) {
//        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/extra.fxml");
//    }
}
