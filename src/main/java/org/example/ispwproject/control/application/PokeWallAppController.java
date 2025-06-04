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
            // recupero dell'username dalla sessione
            String username = getUsernameFromSession(sessionId);

            // recupero di tutti i post che l'utente con quell'username non ha ancora visualizzato
            List<PokeWall> unseenPosts = pokeWallDAO.getUnseenPosts(username); // lista di post non ancora visualizzati dall'utente

            // si itera su ogni oggetto PokeWall presente nella lista
            for (PokeWall post : unseenPosts) {
                post.registerObserver(observer);     // Aggiunta observer a ogni post non visto
                observer.update(post);               // Notifica immediata del post
                pokeWallDAO.markPostAsSeen(post.getId(), username); // Segna post come visto
            }

            // si evitano i duplicati, perchè si controlla se l'observer non è già presente nella lista observers
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

        // recupero della sessione e controllo
        Session session = SessionManager.getSessionManager().getSessionFromId(sessionId);
        if (session == null) {
            throw new SystemException("Session not found. Please log in again.");
        }
        // estrazione dell'id dell'utente associato alla sessione recuperata
        String userId = session.getUserId();
        // recupero user associato all'id dallo strato di persistenza e controllo
        User user = userDAO.read(userId);
        if (user == null) {
            throw new SystemException("User not found: " + userId);
        }

        // instanziazione di un nuovo oggetto PokeWallBean
        PokeWallBean pokeWallBean = new PokeWallBean();
        // associo il nome fornito come parametro del metodo al campo PokeWallBean
        pokeWallBean.setPokeName(pokeName);
        // associo la size al campo PokeWallBean, recuperandola dalla bean di pokeLab
        pokeWallBean.setSize(pokeLabBean.getBowlSize());
        // associo tutti gli ingredienti al campo PokeWallBean, recuperandoli dalla bean di pokeLab
        pokeWallBean.setIngredients(
                pokeLabBean.getAllIngredients().entrySet().stream()
                        .map(entry -> entry.getKey() + ": " + entry.getValue().toString().toLowerCase())
                        .toList()
        );

        /* creazione di un nuovo oggetto PokeWall passandogli dei parametri:
            0-> indica l'id di un nuovo post, che poi verrà sovrascritto dal DAO quando si fa il create
            il nome del poke da visualizzare, la size, lo username dell'utente che ha creato il post e la lista di ingredienti*/
        PokeWall pokeWall = new PokeWall(
                0,
                pokeWallBean.getPokeName(),
                pokeWallBean.getSize(),
                user.getUsername(),
                pokeWallBean.getIngredients()
        );

        pokeWall.registerObserver(this); // registro l'observer sul nuovo PokeWall
        pokeWallDAO.create(pokeWall); // richiamo la DAO per creare effettivamente il nuovo PokeWall sullo strato di persistenza
        pokeWall.notifyObservers(); // notifica tutti gli observer registrati

        return true;
    }

    // restituisce tutti i post presenti nel Poke Wall
    public List<PokeWall> getAllPosts() throws SystemException {
        return pokeWallDAO.getAllPosts();
    }

    // verifica se un utente può eliminare un post oppure no
    public boolean canUserDeletePost(String username, int postId) throws SystemException {
        // si richiama il metodo per ottenere il post corrispondente all'id passato come parametro
        PokeWall post = getPostById(postId);
        // ritorna true se il post esiste e lo username del creatore di quel post corrisponde con lo username passato come parametro
        return post != null && post.getUsername().equals(username);
    }

    // elimina un post solo se l'utente che lo richiede è anche l'autore
    public boolean deletePost(int postId, String requestingUsername) throws SystemException {
        // se il metodo precedente ritorna false, non fa eliminare il post
        if (!canUserDeletePost(requestingUsername, postId)) {
            return false;
        }
        // se il metodo precedente ritorna true, fa eliminare il post
        pokeWallDAO.delete(postId);
        return true;
    }


    // restituisce il post associato ad un determinato ID
    public PokeWall getPostById(int postId) throws SystemException {
        List<PokeWall> posts = pokeWallDAO.getAllPosts(); // lista di tutti i post nel PokeWall
        // scorriamo su tutta la lista dei post presenti sul PokeWall
        for (PokeWall pokeWall : posts) {
            if (pokeWall.getId() == postId) { // cerco il post relativo all'ID che mi interessa
                // se lo trovo lo ritorno
                return pokeWall;
            }
        }
        // se non lo trovo ritorno null
        return null;
    }

    // recupera lo username dell'utente associato ad una sessione
    private String getUsernameFromSession(int sessionId) throws SystemException {
        // recupero la sessione
        Session session = SessionManager.getSessionManager().getSessionFromId(sessionId);
        // recupero lo user associato a quella sessione e controllo
        User user = userDAO.read(session.getUserId());
        if (user == null) {
            throw new SystemException("User not found for session id: " + sessionId);
        }
        return user.getUsername();
    }


    // notifica tutti gli observer dell'aggiunta di un nuovo post
    @Override
    public void update(PokeWall newPost) {
        // scorre la lista di observer
        for (PokeWallObserver observer : observers) {
            // propaga la notifica del nuovo post per ogni observer
            observer.update(newPost);
        }
    }
}
