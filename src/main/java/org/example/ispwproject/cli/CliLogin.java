package org.example.ispwproject.cli;

import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.control.application.LoginAppController;
import org.example.ispwproject.utils.bean.CredentialBean;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.UserBean;
import org.example.ispwproject.utils.enumeration.UserType;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class CliLogin extends CliController{
    private static final Logger logger = Logger.getLogger(CliLogin.class.getName());


    private int sId;
    private PokeLabBean pokeLabBean;

    @Override
    public void init(int sId, PokeLabBean pokeLabBean)throws SystemException, IOException, LoginException, SQLException {
        this.sId = sId;
        this.pokeLabBean = pokeLabBean;
    }

    public void register(String uid, String password, String email, UserType userType, String address){
        UserBean userBean = new UserBean(uid, password, email, userType, address);
        LoginAppController loginAppController = new LoginAppController();
        loginAppController.register(userBean);
        logger.info("Registration completed successfully.");
    }

    public void login(int sId, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        init(sId, pokeLabBean);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        logger.info("Login");

        logger.info("Insert username: ");
        String username = bufferedReader.readLine();

        logger.info("Insert password: ");
        String password = bufferedReader.readLine();

        LoginAppController loginAppController = new LoginAppController();
        CredentialBean credentialBean = new CredentialBean(username, password);
        int sessionId = loginAppController.login(credentialBean);


        SessionManager sessionManager = SessionManager.getSessionManager();
        Session session = sessionManager.getSessionFromId(sessionId);
        System.out.println();

        if (sessionId != -1) {
            logger.info("Hi, " + session.getUserId());
            new CliHomePage().init(sId, pokeLabBean);
        } else {
            logger.warning("Invalid username or password.");
            handleFailedLogin();
        }
    }

    private void handleFailedLogin() throws SystemException, IOException, LoginException, SQLException {
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
