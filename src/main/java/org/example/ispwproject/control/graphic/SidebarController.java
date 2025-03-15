package org.example.ispwproject.control.graphic;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.control.application.LoginAppController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.UserTypeBean;
import org.example.ispwproject.utils.enumeration.UserType;

public class SidebarController {
    @FXML
    private Label userLabel;

    private int id = -1;
    private Session session;

    public void initialize(){
        SessionManager sessionManager = SessionManager.getSessionManager();
        this.id = sessionManager.getCurrentId();
        session = sessionManager.getSessionFromId(id);
        if (id != -1){
            userLabel.setText("Hi, " + session.getUserId());
        } else {
            userLabel.setText("Guest");
        }
    }

    @FXML
    public void handleHomeCLick(MouseEvent event) {
        System.out.println("Vai alla home");
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/homePage.fxml", null, id);
    }


    @FXML
    public void handleLogInCLick(MouseEvent event) {
        System.out.println("Logging in...");
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/login.fxml", null, id);
    }


    @FXML
    public void handleBuyPokeLabCLick(MouseEvent event) {
        System.out.println("Crea il tuo pokè!");

        LoginAppController loginAppController = new LoginAppController();
        if (session != null){
            UserTypeBean userTypeBean = new UserTypeBean(session.getUserId(), UserType.PREMIUMUSER);
            if (loginAppController.userType(userTypeBean)){
                BuyPokeLabAppController buyPokeLabAppController = new BuyPokeLabAppController();
                PokeLabBean pokeLabBean = buyPokeLabAppController.newPokeLab();

                //SaveOrRecoverBean dataBean = new SaveOrRecoverBean(session.getUserId());
               // boolean value = buyPokeLabAppController.checkGuitar(dataBean);
                //BuyDreamGuitarControllerStart.setToRecover(value);
                ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", null, id);
            }
        } else{
            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/login.fxml", null, id);
        }

    }

    @FXML
    public void handlePostOnPokeWallCLick(MouseEvent event) {
        System.out.println("Posta su pokè wall");
    }


}