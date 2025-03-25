package org.example.ispwproject.control.application;

import org.example.ispwproject.model.pokewall.PokeWall;
import org.example.ispwproject.model.pokewall.PokeWallDAO;
import org.example.ispwproject.model.pokewall.FSPokeWallDAO;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.model.user.User;
import org.example.ispwproject.model.user.UserDAO;
import org.example.ispwproject.utils.dao.DAOFactory;
import org.example.ispwproject.utils.exception.SystemException;

import java.util.ArrayList;
import java.util.List;

public class PokeWallAppController {

    private PokeWallDAO pokeWallDAO;
    private UserDAO userDAO;

    // Costruttore che inizializza le DAO (scegliendo quella in memoria o su file system)
    public PokeWallAppController() {
        // Usa il DAO per il PokeWall dal file system o in memoria
        this.pokeWallDAO = new FSPokeWallDAO();  // oppure InMemoryPokeWallDAO.getInstance()
        // Inizializza il DAO per gli utenti
        DAOFactory daoFactory = DAOFactory.getInstance();
        userDAO = daoFactory.getUserDAO();
    }

    /**
     * Metodo per creare un nuovo post sulla PokeWall
     * @param saveBean - informazioni sull'utente che sta creando il post
     * @param content - contenuto del post
     * @return true se il post è stato creato con successo, false altrimenti
     */
    public boolean createPost(SaveBean saveBean, String content, List<String> ingredients) throws SystemException {
        String userId = saveBean.getUid();
        User user = userDAO.read(userId);

        if (user == null) {
            System.out.println("User not found!");
            return false;
        }

        PokeWall pokeWall = new PokeWall(content, user.getUsername(), ingredients);
        pokeWallDAO.create(pokeWall);
        System.out.println("Post created successfully!");
        return true;
    }


    /**
     * Metodo per recuperare tutti i post presenti nel PokeWall
     * @return una lista di tutti i post
     */
    public List<String> getAllPosts() throws SystemException {
        List<PokeWall> posts = pokeWallDAO.getAllPosts(); // Ottieni i post dalla persistenza
        List<String> postStrings = new ArrayList<>();

        // Converte ogni post in una stringa
        for (PokeWall post : posts) {
            String postContent = post.getContent() + " (by " + post.getUsername() + ")";
            postStrings.add(postContent);  // Aggiungi la stringa alla lista
        }

        return postStrings;
    }

    /**
     * Metodo per eliminare un post dalla PokeWall
     * @param postId - ID del post da eliminare
     * @return true se il post è stato eliminato con successo, false altrimenti
     */
    public boolean deletePost(int postId) throws SystemException {
        try {
            pokeWallDAO.delete(postId);
            System.out.println("Post deleted successfully!");
            return true;
        } catch (SystemException e) {
            System.out.println("Failed to delete post.");
            return false;
        }
    }

    /**
     * Metodo per recuperare un singolo post dalla PokeWall tramite ID
     * @param postId - ID del post
     * @return il post richiesto
     */
    public PokeWall getPostById(int postId) throws SystemException {
        List<PokeWall> posts = pokeWallDAO.getAllPosts();
        for (PokeWall pokeWall : posts) {
            if (pokeWall.getId() == postId) {
                return pokeWall;
            }
        }
        return null; // Se non trovato
    }
}
