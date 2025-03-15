package org.example.ispwproject.control.graphic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.control.application.LoginAppController;
import org.example.ispwproject.utils.bean.CredentialBean;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.UserBean;
import org.example.ispwproject.utils.enumeration.UserType;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController extends GraphicController{
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private int sessionId = -1;

    public void initialize(int sessionId, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        this.sessionId = sessionId;
    }

    public void registerUser(String Uid, String password, String email, UserType uType, String address){
        UserBean userBean = new UserBean(Uid, password, email, uType, address);
        LoginAppController loginAppController = new LoginAppController();
        loginAppController.register(userBean);
    }

    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        LoginAppController loginAppController =new LoginAppController();
        CredentialBean credentialBean = new CredentialBean(username, password);
        sessionId = loginAppController.login(credentialBean);
        if(sessionId != -1){
            ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/homePage.fxml", null, sessionId);
        } else{
            errorLabel.setText("Invalid username or password");
        }
    }

    public void handleBackToHome(ActionEvent event) {
        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/homePage.fxml", null, sessionId);
    }
}
