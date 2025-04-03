package org.example.ispwproject.control.graphic.buypokelab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;

public class BowlSizeController extends GraphicController {

    @FXML private RadioButton smallSize;
    @FXML private RadioButton mediumSize;
    @FXML private RadioButton largeSize;
    @FXML private Button confirmButton;

    private ToggleGroup sizeToggleGroup; // Non è più @FXML, viene gestito manualmente
    private PokeLabBean pokeLabBean;
    private BuyPokeLabAppController buyPokeLabAppController;
    private int sessionId;

    @Override
    public void init(int sessionId, PokeLabBean pokeLabBean) {
        this.pokeLabBean = pokeLabBean;
        this.buyPokeLabAppController = new BuyPokeLabAppController();
        this.sessionId = sessionId;


        // Imposta la selezione corrente in base al valore salvato
        if (pokeLabBean != null) {
            String currentSize = pokeLabBean.getBowlSize();
            if ("Small".equals(currentSize)) {
                smallSize.setSelected(true);
            } else if ("Medium".equals(currentSize)) {
                mediumSize.setSelected(true);
            } else if ("Large".equals(currentSize)) {
                largeSize.setSelected(true);
            }
        }
    }

    @FXML
    private void initialize() {
        // Creazione del ToggleGroup
        sizeToggleGroup = new ToggleGroup();

        // Associa il ToggleGroup ai RadioButton
        smallSize.setToggleGroup(sizeToggleGroup);
        mediumSize.setToggleGroup(sizeToggleGroup);
        largeSize.setToggleGroup(sizeToggleGroup);

        // Disabilita il bottone di conferma inizialmente
        confirmButton.setDisable(true);

        // Aggiungi l'evento per attivare/disattivare il bottone quando si seleziona un RadioButton
        smallSize.setOnAction(e -> updateConfirmButtonState());
        mediumSize.setOnAction(e -> updateConfirmButtonState());
        largeSize.setOnAction(e -> updateConfirmButtonState());
    }

    // Metodo per abilitare/disabilitare il bottone di conferma
    private void updateConfirmButtonState() {
        confirmButton.setDisable(
                !smallSize.isSelected() && !mediumSize.isSelected() && !largeSize.isSelected()
        );
    }

    @FXML
    private void handleConfirmSelection(ActionEvent event) {
        String selectedSize = null;

        // Verifica quale radio button è selezionato
        if (smallSize.isSelected()) {
            selectedSize = "Small";
        } else if (mediumSize.isSelected()) {
            selectedSize = "Medium";
        } else if (largeSize.isSelected()) {
            selectedSize = "Large";
        }

        // Se la selezione è valida, aggiorna il bean
        if (selectedSize != null && pokeLabBean != null) {
            if (selectedSize.equals(pokeLabBean.getBowlSize())) {
                ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, pokeLabBean.getId());
                return;
            }

            pokeLabBean.setBowlSize(selectedSize);  // Aggiorna solo il bean

            SessionManager sessionManager = SessionManager.getSessionManager();
            Session session = sessionManager.getSessionFromId(sessionId);
            String userId = session.getUserId();

            SaveBean saveBean = new SaveBean(userId);

            try {
                if (!buyPokeLabAppController.savePokeLab(pokeLabBean, saveBean)) {
                    System.out.println("Error saving bowl size");
                    return;
                }
                System.out.println("Bowl size saved: " + selectedSize);
            } catch (Exception e) {
                System.out.println("Error saving bowl size: " + e.getMessage());
            }
        }

        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, pokeLabBean.getId());
    }
}

