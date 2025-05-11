package org.example.ispwproject.control.graphic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.enumeration.FortuneMessage;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class FortuneCookieController extends GraphicController {
    private PokeLabBean pokeLabBean;
    private int id;

    @FXML
    private Label fortuneLabel;

    @FXML
    private void showFortune() {
        // Get a random motivational quote and display it in the Label
        fortuneLabel.setText(FortuneMessage.getRandomFortune().getMessage());
    }

    @Override
    public void init(int sessionId, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        this.pokeLabBean = pokeLabBean;
        this.id = sessionId;
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/pokeWall.fxml", pokeLabBean, id);
    }

}

