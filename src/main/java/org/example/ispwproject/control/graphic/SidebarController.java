package org.example.ispwproject.control.graphic;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.NavigationHelper;
import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;

public class SidebarController {
    @FXML
    private Label userLabel;

    private int id = -1;
    private Session session;

    private static final String LOGIN_SCENE_PATH = System.getProperty("login.scene.path", "/org/example/ispwproject/view/login.fxml");

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
        ChangePage.changeScene((Node) event.getSource(), LOGIN_SCENE_PATH, null, id);
    }


    @FXML
    public void handleBuyPokeLabCLick(MouseEvent event) throws SystemException, LoginException {
        NavigationHelper.handleSceneChange(event, session, "/org/example/ispwproject/view/buyPokeLab.fxml", id);
    }


    @FXML
    public void handlePokeWallCLick(MouseEvent event) throws SystemException, LoginException {
        NavigationHelper.handleSceneChange(event, session, "/org/example/ispwproject/view/pokeWall.fxml", id);
    }


}