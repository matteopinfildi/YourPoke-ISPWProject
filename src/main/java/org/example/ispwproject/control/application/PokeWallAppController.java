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
import java.util.logging.Logger;

public class PokeWallAppController implements PokeWallObserver {

    private final PokeWallDAO pokeWallDAO;
    private final UserDAO userDAO;
    private static final PokeWallAppController instance = new PokeWallAppController();
    private final List<PokeWallObserver> observers = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(PokeWallAppController.class.getName());

    private PokeWallAppController() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        this.pokeWallDAO = daoFactory.getPokeWallDAO();
        this.userDAO = daoFactory.getUserDAO();
    }

    // restituisce l'istanza singleton del controller
    public static PokeWallAppController getInstance() {
        return instance;
    }

    // registra un osservatore e notifica l'aggiunta dei nuovi post non ancora visualizzati da un utente
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

        } catch (SystemException e) {
            logger.severe("Error occurred while registering observer: " + e.getMessage());
        }
    }

    // rimuove un osservatore dalla lista degli observer registrati
    public void removeObserver(PokeWallObserver observer) {
        observers.remove(observer);
    }

    // crea un nuovo post nella PokeWall associato all'utente e notifica gli observer
    public boolean createPost(SaveBean saveBean, PokeWallBean pokeWallBean) throws SystemException {
        String userId = saveBean.getUid();
        User user = userDAO.read(userId);
        if (user == null) {
            return false;
        }

        PokeWall pokeWall = new PokeWall(0, pokeWallBean.getPokeName(), pokeWallBean.getSize(),
                user.getUsername(), pokeWallBean.getIngredients());

        pokeWall.registerObserver(this); // Questo controller "osserva" il post
        pokeWallDAO.create(pokeWall);
        pokeWall.notifyObservers(); // Notifica tutti gli observer

        return true;
    }

    // restituisce tutti i post presenti nel Poke Wall
    public List<PokeWall> getAllPosts() throws SystemException {
        return pokeWallDAO.getAllPosts();
    }

    // elimina un post solo se l'utente che lo richiede è anche l'autore
    public boolean deletePost(int postId, String requestingUsername) throws SystemException {
        PokeWall postToDelete = getPostById(postId);
        if (postToDelete == null || !postToDelete.getUsername().equals(requestingUsername)) {
            return false;
        }
        pokeWallDAO.delete(postId);
        return true;
    }


    // restituisce il post associato ad un determinato ID
    public PokeWall getPostById(int postId) throws SystemException {
        List<PokeWall> posts = pokeWallDAO.getAllPosts();
        for (PokeWall pokeWall : posts) {
            if (pokeWall.getId() == postId) {
                return pokeWall;
            }
        }
        return null;
    }

    // recupera lo username dell'utente associato ad una sessione
    private String getUsernameFromSession(int sessionId) throws SystemException {
        Session session = SessionManager.getSessionManager().getSessionFromId(sessionId);
        User user = userDAO.read(session.getUserId());
        if (user == null) {
            throw new SystemException("User not found for session id: " + sessionId);
        }
        return user.getUsername();
    }

    // notifica tutti gli observer dell'aggiunta di un nuovo post
    @Override
    public void update(PokeWall newPost) {
        for (PokeWallObserver observer : observers) {
            observer.update(newPost);
        }
    }
}
