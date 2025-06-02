package org.example.ispwproject.control.application;

import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.control.PokeWallObserver;
import org.example.ispwproject.model.observer.pokewall.PokeWall;
import org.example.ispwproject.model.observer.pokewall.PokeWallDAO;
import org.example.ispwproject.model.user.User;
import org.example.ispwproject.model.user.UserDAO;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.PokeWallBean;
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

            List<PokeWall> unseenPosts = pokeWallDAO.getUnseenPosts(username); // lista di post non ancora visualizzati dall'utente

            for (PokeWall post : unseenPosts) {
                post.registerObserver(observer);     // Aggiunta observer a ogni post non visto
                observer.update(post);               // Notifica immediata del post
                pokeWallDAO.markPostAsSeen(post.getId(), username); // Segna post come visto
            }

            if (!observers.contains(observer)) {
                observers.add(observer);  // Salvataggio observer solo se non è già presente
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
    public boolean createPost(int sessionId, String pokeName, PokeLabBean pokeLabBean) throws SystemException {

        Session session = SessionManager.getSessionManager().getSessionFromId(sessionId);
        if (session == null) {
            throw new SystemException("Session not found. Please log in again.");
        }
        String userId = session.getUserId();
        User user = userDAO.read(userId);
        if (user == null) {
            throw new SystemException("User not found: " + userId);
        }

        PokeWallBean pokeWallBean = new PokeWallBean();
        pokeWallBean.setPokeName(pokeName);
        pokeWallBean.setSize(pokeLabBean.getBowlSize());
        pokeWallBean.setIngredients(
                pokeLabBean.getAllIngredients().entrySet().stream()
                        .map(entry -> entry.getKey() + ": " + entry.getValue().toString().toLowerCase())
                        .toList()
        );

        PokeWall pokeWall = new PokeWall(
                0,
                pokeWallBean.getPokeName(),
                pokeWallBean.getSize(),
                user.getUsername(),
                pokeWallBean.getIngredients()
        );

        pokeWall.registerObserver(this);
        pokeWallDAO.create(pokeWall);
        pokeWall.notifyObservers(); // notifica tutti gli observer

        return true;
    }

    // restituisce tutti i post presenti nel Poke Wall
    public List<PokeWall> getAllPosts() throws SystemException {
        return pokeWallDAO.getAllPosts();
    }

    public boolean canUserDeletePost(String username, int postId) throws SystemException {
        PokeWall post = getPostById(postId);
        return post != null && post.getUsername().equals(username);
    }

    // elimina un post solo se l'utente che lo richiede è anche l'autore
    public boolean deletePost(int postId, String requestingUsername) throws SystemException {
        if (!canUserDeletePost(requestingUsername, postId)) {
            return false;
        }
        pokeWallDAO.delete(postId);
        return true;
    }


    // restituisce il post associato ad un determinato ID
    public PokeWall getPostById(int postId) throws SystemException {
        List<PokeWall> posts = pokeWallDAO.getAllPosts(); // lista di tutti i post nel PokeWall
        for (PokeWall pokeWall : posts) {
            if (pokeWall.getId() == postId) { // cerco il post relativo all'ID che mi interessa
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

    public String formatPostForDisplay(PokeWall post) {
        StringBuilder postText = new StringBuilder();
        postText.append(post.getUsername())
                .append(" created the Poke Lab: ")
                .append(post.getPokeName())
                .append("\n");

        String pokeSize = (post.getSize() == null || post.getSize().isEmpty())
                ? "Unknown size"
                : post.getSize();
        postText.append("Poke size: ").append(pokeSize).append("\n");

        for (String ingredient : post.getIngredients()) {
            postText.append("- ").append(ingredient).append("\n");
        }

        return postText.toString();
    }

    public String generateNotificationMessage(PokeWall newPost) {
        return "New post by " + newPost.getUsername() + ": " + newPost.getPokeName();
    }

    // notifica tutti gli observer dell'aggiunta di un nuovo post
    @Override
    public void update(PokeWall newPost) {
        for (PokeWallObserver observer : observers) {
            observer.update(newPost);
        }
    }
}
