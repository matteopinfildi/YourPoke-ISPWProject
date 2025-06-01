package org.example.ispwproject;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.control.application.LoginAppController;
import org.example.ispwproject.control.graphic.buypokelab.BuyPokeLabController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.bean.UserTypeBean;
import org.example.ispwproject.utils.enumeration.UserType;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;

public class NavigationHelper {

    private NavigationHelper() {
        // Costruttore vuoto
    }

    /* verifica sessione e tipo di utente, se e' premium prepara una nuova PokeLabBean
     e imposta il flag di recupero nel controller e cambia scena */
    public static void handleSceneChange(ActionEvent event, Session session, String scenePath, int id) throws SystemException, LoginException {
        LoginAppController loginAppController = new LoginAppController();
        if (session != null) {
            UserTypeBean userTypeBean = new UserTypeBean(session.getUserId(), UserType.PREMIUMUSER);
            if (loginAppController.userType(userTypeBean)) {
                BuyPokeLabAppController buyPokeLabAppController = new BuyPokeLabAppController();
                PokeLabBean pokeLabBean = buyPokeLabAppController.newPokeLab();
                SaveBean saveBean = new SaveBean(session.getUserId());
                boolean value = buyPokeLabAppController.checkPokeLab(saveBean);
                BuyPokeLabController.setRetrieve(value);
                ChangePage.changeScene((Node) event.getSource(), scenePath, pokeLabBean, id);
            }
        } else {
            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/login.fxml", null, id);
        }
    }

    public static void handleSceneChange(MouseEvent event, Session session, String scenePath, int id) throws  SystemException, LoginException {
        LoginAppController loginAppController = new LoginAppController();
        if (session != null) {
            UserTypeBean userTypeBean = new UserTypeBean(session.getUserId(), UserType.PREMIUMUSER);
            if (loginAppController.userType(userTypeBean)) {
                BuyPokeLabAppController buyPokeLabAppController = new BuyPokeLabAppController();
                PokeLabBean pokeLabBean = buyPokeLabAppController.newPokeLab();
                SaveBean saveBean = new SaveBean(session.getUserId());
                boolean value = buyPokeLabAppController.checkPokeLab(saveBean);
                BuyPokeLabController.setRetrieve(value);
                ChangePage.changeScene((Node) event.getSource(), scenePath, pokeLabBean, id);
            }
        } else {
            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/login.fxml", null, id);
        }
    }
}