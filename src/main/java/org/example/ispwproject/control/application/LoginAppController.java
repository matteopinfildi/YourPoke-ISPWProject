package org.example.ispwproject.control.application;

import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.model.user.User;
import org.example.ispwproject.model.user.UserDAO;
import org.example.ispwproject.utils.bean.CredentialBean;
import org.example.ispwproject.utils.bean.UserBean;
import org.example.ispwproject.utils.bean.UserTypeBean;
import org.example.ispwproject.utils.dao.DAOFactory;
import org.example.ispwproject.utils.enumeration.UserType;

import javax.security.auth.login.LoginException;

public class LoginAppController {

    DAOFactory daoFactory;
    UserDAO userDAO;

    public LoginAppController(){
        daoFactory = DAOFactory.getInstance();
        userDAO = daoFactory.getUserDAO();
    }

    public void register(UserBean userBean) throws LoginException {

        // viene creato un oggetto di dominio, passando il costruttore di userBean
        User userA = new User(userBean);
        try {
            // viene richiamato lo strato di persistenza per effettuare la create
            userDAO.create(userA);
        } catch (org.example.ispwproject.utils.exception.SystemException e) {
            throw new LoginException("Errore" + e.getMessage());
        }

    }

    // metodo per effettuare il login, restituisce -1 se il login fallisce
    public int login(CredentialBean credentialBean) throws LoginException {
        User userA = null;

        // si recupera username e password
        String username = credentialBean.getUsername();
        String password = credentialBean.getPassword();
        try{
            // si cerca l'utente sullo strato di persistenza
            userA = userDAO.read(username);
        } catch (org.example.ispwproject.utils.exception.SystemException e) {
            throw new LoginException("Errore" + e.getMessage());
        }

        /* si controlla che lo user non sia null e che la password e lo username inseriti in input
           siano uguali a quelli estratti dallo strato di persistenza */
        if (userA != null && userA.getUsername().equals(username) && userA.getPassword().equals(password)) {
            // recupero dell'istanza singleton della sessione'
            SessionManager manager = SessionManager.getSessionManager();
            // creazione di una nuova sessione
            Session session = manager.createSession(username);
            // registrazione della sessione appena creata
            manager.addSession(session);
            // viene restituito l'intero univoco associato alla sessione appena creata
            return session.getSessionId();
        }
        // se qualcosa va storto ritorna -1
        return -1;
    }

    public boolean userType(UserTypeBean userTypeBean) throws LoginException {
        User userA = null;

        // recupera l'id dell'utente
        String uid = userTypeBean.getUid();
        // recupera il tipo di utente
        UserType uType = userTypeBean.getuType();
        try {
            // recupero dell'utente associato all'id dallo strato di persistenza
            userA = userDAO.read(uid);
        } catch (org.example.ispwproject.utils.exception.SystemException e) {
            throw new LoginException("Errore" + e.getMessage());
        }

        // confronto del valore atteso e il valore recuperato relativo allo userA
        return uType == userA.getuType();
    }
}
