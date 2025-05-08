//package org.example.ispwproject.control.application;
//
//import org.example.ispwproject.Session;
//import org.example.ispwproject.SessionManager;
//import org.example.ispwproject.control.graphic.pokewall.PokeWallController;
//import org.example.ispwproject.model.PokeWallObserver;
//import org.example.ispwproject.model.observer.pokewall.PokeWall;
//import org.example.ispwproject.model.observer.pokewall.PokeWallDAO;
//import org.example.ispwproject.utils.bean.PokeWallBean;
//import org.example.ispwproject.utils.bean.SaveBean;
//import org.example.ispwproject.model.user.User;
//import org.example.ispwproject.model.user.UserDAO;
//import org.example.ispwproject.utils.dao.DAOFactory;
//import org.example.ispwproject.utils.exception.SystemException;
//
//import java.util.List;
//
//public class PokeWallAppController {
//    private final PokeWallDAO pokeWallDAO;
//    private final UserDAO userDAO;
//    private static final PokeWallAppController instance = new PokeWallAppController();
//
//    private PokeWallAppController() {
//        DAOFactory daoFactory = DAOFactory.getInstance();
//        this.pokeWallDAO = daoFactory.getPokeWallDAO();
//        this.userDAO = daoFactory.getUserDAO();
//    }
//
//    public static PokeWallAppController getInstance() {
//        return instance;
//    }
//
//    public void registerObserver(PokeWallObserver observer, int sessionId) {
//        try {
//            String username = getUsernameFromSession(sessionId);
//            if (observer instanceof PokeWallController pokeWallController) {
//                pokeWallController.setCurrentUsername(username);
//
//                List<PokeWall> unseenPosts = pokeWallDAO.getUnseenPosts(username);
//
//                List<PokeWall> allPosts = pokeWallDAO.getAllPosts();
//                for (PokeWall post : allPosts) {
//                    post.removeObserver(observer);
//                }
//
//                for (PokeWall post : unseenPosts) {
//                    post.registerObserver(observer);
//                    observer.update(post);
//                    pokeWallDAO.markPostAsSeen(post.getId(), username);
//                }
//            }
//        } catch (SystemException ignored) {
//            // Puoi eventualmente loggare l'errore se hai un sistema di logging
//        }
//    }
//
//    public boolean createPost(SaveBean saveBean, PokeWallBean pokeWallBean) throws SystemException {
//        String userId = saveBean.getUid();
//        User user = userDAO.read(userId);
//        if (user == null) {
//            return false;
//        }
//
//        PokeWall pokeWall = new PokeWall(0, pokeWallBean.getPokeName(), pokeWallBean.getSize(),
//                user.getUsername(), pokeWallBean.getIngredients());
//
//        pokeWallDAO.create(pokeWall);
//        pokeWall.notifyObservers();
//
//        return true;
//    }
//
//    public List<PokeWall> getAllPosts() throws SystemException {
//        return pokeWallDAO.getAllPosts();
//    }
//
//    public boolean deletePost(int postId, String requestingUsername) throws SystemException {
//        try {
//            PokeWall postToDelete = getPostById(postId);
//            if (postToDelete == null) {
//                return false;
//            }
//
//            if (!postToDelete.getUsername().equals(requestingUsername)) {
//                return false;
//            }
//
//            pokeWallDAO.delete(postId);
//            return true;
//        } catch (SystemException e) {
//            throw e;
//        }
//    }
//
//    public PokeWall getPostById(int postId) throws SystemException {
//        List<PokeWall> posts = pokeWallDAO.getAllPosts();
//        for (PokeWall pokeWall : posts) {
//            if (pokeWall.getId() == postId) {
//                return pokeWall;
//            }
//        }
//        return null;
//    }
//
//    private String getUsernameFromSession(int sessionId) throws SystemException {
//        Session session = SessionManager.getSessionManager().getSessionFromId(sessionId);
//        User user = userDAO.read(session.getUserId());
//        if (user == null) {
//            throw new SystemException("User not found for session id: " + sessionId);
//        }
//        return user.getUsername();
//    }
//}

package org.example.ispwproject.control.application;

import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.model.PokeWallObserver;
import org.example.ispwproject.model.observer.pokewall.PokeWall;
import org.example.ispwproject.model.observer.pokewall.PokeWallDAO;
import org.example.ispwproject.model.user.User;
import org.example.ispwproject.model.user.UserDAO;
import org.example.ispwproject.utils.bean.PokeWallBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.dao.DAOFactory;
import org.example.ispwproject.utils.exception.SystemException;

import java.util.ArrayList;
import java.util.List;

public class PokeWallAppController implements PokeWallObserver {

    private final PokeWallDAO pokeWallDAO;
    private final UserDAO userDAO;
    private static final PokeWallAppController instance = new PokeWallAppController();
    private final List<PokeWallObserver> observers = new ArrayList<>();

    private PokeWallAppController() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        this.pokeWallDAO = daoFactory.getPokeWallDAO();
        this.userDAO = daoFactory.getUserDAO();
    }

    public static PokeWallAppController getInstance() {
        return instance;
    }

    public void registerObserver(PokeWallObserver observer, int sessionId) {
        try {
            String username = getUsernameFromSession(sessionId);

            List<PokeWall> unseenPosts = pokeWallDAO.getUnseenPosts(username);

            for (PokeWall post : unseenPosts) {
                post.registerObserver(observer);     // Aggiungiamo observer a ogni post non visto
                observer.update(post);               // Notifica immediata del post
                pokeWallDAO.markPostAsSeen(post.getId(), username); // Segna come visto
            }

            if (!observers.contains(observer)) {
                observers.add(observer);  // Salviamo observer solo se non è già presente
            }

        } catch (SystemException ignored) {
            // TODO: logga eccezione
        }
    }

    public void removeObserver(PokeWallObserver observer) {
        observers.remove(observer);
    }

    public boolean createPost(SaveBean saveBean, PokeWallBean pokeWallBean) throws SystemException {
        String userId = saveBean.getUid();
        User user = userDAO.read(userId);
        if (user == null) {
            return false;
        }

        PokeWall pokeWall = new PokeWall(0, pokeWallBean.getPokeName(), pokeWallBean.getSize(),
                user.getUsername(), pokeWallBean.getIngredients());

        pokeWall.registerObserver(this); // Questo controller osserva il post
        pokeWallDAO.create(pokeWall);
        pokeWall.notifyObservers(); // Notifica tutti gli osservatori

        return true;
    }

    public List<PokeWall> getAllPosts() throws SystemException {
        return pokeWallDAO.getAllPosts();
    }

    public boolean deletePost(int postId, String requestingUsername) throws SystemException {
        try {
            PokeWall postToDelete = getPostById(postId);
            if (postToDelete == null || !postToDelete.getUsername().equals(requestingUsername)) {
                return false;
            }
            pokeWallDAO.delete(postId);
            return true;
        } catch (SystemException e) {
            throw e;
        }
    }

    public PokeWall getPostById(int postId) throws SystemException {
        List<PokeWall> posts = pokeWallDAO.getAllPosts();
        for (PokeWall pokeWall : posts) {
            if (pokeWall.getId() == postId) {
                return pokeWall;
            }
        }
        return null;
    }

    private String getUsernameFromSession(int sessionId) throws SystemException {
        Session session = SessionManager.getSessionManager().getSessionFromId(sessionId);
        User user = userDAO.read(session.getUserId());
        if (user == null) {
            throw new SystemException("User not found for session id: " + sessionId);
        }
        return user.getUsername();
    }

    @Override
    public void update(PokeWall newPost) {
        for (PokeWallObserver observer : observers) {
            observer.update(newPost);
        }
    }
}
