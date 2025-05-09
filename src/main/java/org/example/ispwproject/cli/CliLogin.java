package org.example.ispwproject.cli;

import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.control.application.LoginAppController;
import org.example.ispwproject.utils.bean.CredentialBean;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.UserBean;
import org.example.ispwproject.utils.enumeration.UserType;
import org.example.ispwproject.utils.exception.CliException;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class CliLogin extends CliController{

    private int sId;
    private PokeLabBean pokeLabBean;

    @Override
    public void init(int sId, PokeLabBean pokeLabBean)throws SystemException, IOException, LoginException, SQLException {
        this.sId = sId;
        this.pokeLabBean = pokeLabBean;
    }

    public void register(String uid, String password, String email, UserType userType, String address) throws LoginException {
        UserBean userBean = new UserBean(uid, password, email, userType, address);
        LoginAppController loginAppController = new LoginAppController();
        loginAppController.register(userBean);
        System.out.println("Registration completed successfully.");
    }

    public void login(int sId, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException, CliException {
        init(sId, pokeLabBean);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Login");

        System.out.print("Insert username: ");
        String username = bufferedReader.readLine();

        System.out.print("Insert password: ");
        String password = bufferedReader.readLine();

        LoginAppController loginAppController = new LoginAppController();
        CredentialBean credentialBean = new CredentialBean(username, password);
        int sessionId = loginAppController.login(credentialBean);


        SessionManager sessionManager = SessionManager.getSessionManager();
        Session session = sessionManager.getSessionFromId(sessionId);
        System.out.println();

        if (sessionId != -1) {
            System.out.println("Hi, " + session.getUserId());
            new CliHomePage().init(sessionId, pokeLabBean);
        } else {
            System.out.println("Invalid username or password.");
            handleFailedLogin();
        }
    }

    private void handleFailedLogin() throws SystemException, IOException, LoginException, SQLException, CliException {
        boolean condition = true;
        do {
            int selection = userSelection("Login");
            switch (selection) {
                case 1:
                    new CliLogin().init(sId, pokeLabBean);
                    break;

                case 2:
                    new CliHomePage().init(sId, pokeLabBean);
                    break;
                default:
                    System.out.println("Select a valid option!");
            }
        } while (condition);
    }


    @Override
    protected List<String> getAlternative() {return List.of("Try again", " Exit");}
}
