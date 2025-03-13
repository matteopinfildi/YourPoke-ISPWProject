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

public class LoginAppController {

    DAOFactory daoFactory;
    UserDAO userDAO;

    public LoginAppController(){
        daoFactory = DAOFactory.getInstance();
        userDAO = daoFactory.getUserDAO();
    }

    public void register(UserBean userBean) {

        User userA = new User(userBean);
        try {
            userDAO.create(userA);
        } catch (org.example.ispwproject.utils.exception.SystemException e) {
            throw new RuntimeException(e);
        }

    }

    public int login(CredentialBean credentialBean) {
        User userA = null;

        String username = credentialBean.getUsername();
        String password = credentialBean.getPassword();
        try{
            userA = userDAO.read(username);
        } catch (org.example.ispwproject.utils.exception.SystemException e) {
            throw new RuntimeException(e);
        }
        if (userA != null && userA.getUsername().equals(username) && userA.getPassword().equals(password)) {
            SessionManager manager = SessionManager.getSessionManager();
            // il controller passa dati all'esterno sotto forma di bean
            Session session = manager.createSession(username);
            manager.addSession(session);
            return session.getSessionId();
        }
        return -1;
    }

    public boolean userType(UserTypeBean userTypeBean) {
        User userA = null;

        String Uid = userTypeBean.getUid();
        UserType uType = userTypeBean.getuType();
        try {
            userA = userDAO.read(Uid);
        } catch (org.example.ispwproject.utils.exception.SystemException e) {
            throw new RuntimeException(e);
        }
        if (uType != userA.getuType()){
            return false;
        } else {return true;}
    }
}
