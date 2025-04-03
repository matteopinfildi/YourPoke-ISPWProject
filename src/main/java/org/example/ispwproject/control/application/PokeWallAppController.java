package org.example.ispwproject.control.application;

import org.example.ispwproject.model.PokeWallObserver;
import org.example.ispwproject.model.PokeWallSubject;
import org.example.ispwproject.model.pokewall.PokeWall;
import org.example.ispwproject.model.pokewall.PokeWallDAO;
import org.example.ispwproject.model.pokewall.FSPokeWallDAO;
import org.example.ispwproject.utils.bean.PokeWallBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.model.user.User;
import org.example.ispwproject.model.user.UserDAO;
import org.example.ispwproject.utils.dao.DAOFactory;
import org.example.ispwproject.utils.exception.SystemException;

import java.util.ArrayList;
import java.util.List;

public class PokeWallAppController implements PokeWallSubject {

    private PokeWallDAO pokeWallDAO;
    private UserDAO userDAO;
    private List<PokeWallObserver> observers = new ArrayList<>();

    // Costruttore che inizializza le DAO (scegliendo quella in memoria o su file system)
    public PokeWallAppController() {
        // Usa il DAO per il PokeWall dal file system o in memoria
        this.pokeWallDAO = new FSPokeWallDAO();
        // Inizializza il DAO per gli utenti
        DAOFactory daoFactory = DAOFactory.getInstance();
        userDAO = daoFactory.getUserDAO();
    }

    @Override
    public void registerObserver(PokeWallObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(PokeWallObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(PokeWall newPost) {
        for (PokeWallObserver observer : observers) {
            observer.update(newPost);
        }
    }

    public boolean createPost(SaveBean saveBean, PokeWallBean pokeWallBean) throws SystemException {
        String userId = saveBean.getUid();
        User user = userDAO.read(userId);
        if (user == null) {
            System.out.println("User not found!");
            return false;
        }

        // Crea il model dal bean
        PokeWall pokeWall = new PokeWall(
                pokeWallBean.getPokeName(),
                pokeWallBean.getSize(),
                user.getUsername(),
                pokeWallBean.getIngredients()
        );

        pokeWallDAO.create(pokeWall);

        // Notifica tutti gli observer del nuovo post
        notifyObservers(pokeWall);

        return true;
    }



    // Metodo per recuperare tutti i post presenti nel PokeWall
    public List<PokeWall> getAllPosts() throws SystemException {
        List<PokeWall> posts = pokeWallDAO.getAllPosts();
        if (posts == null || posts.isEmpty()) {
            System.out.println("No posts returned from DAO!");
        }
        return posts;
    }




    //Metodo per eliminare un post dalla PokeWall
    public boolean deletePost(int postId, String requestingUsername) throws SystemException {
        try {
            // Prima verifichiamo che il post esista e appartenga all'utente
            PokeWall postToDelete = getPostById(postId);
            if (postToDelete == null) {
                System.out.println("Post not found!");
                return false;
            }

            if (!postToDelete.getUsername().equals(requestingUsername)) {
                System.out.println("User is not authorized to delete this post!");
                return false;
            }

            pokeWallDAO.delete(postId);
            System.out.println("Post deleted successfully!");
            return true;
        } catch (SystemException e) {
            System.out.println("Failed to delete post: " + e.getMessage());
            throw e; // Rilancia l'eccezione per gestione superiore
        }
    }


    //Metodo per recuperare un singolo post dalla PokeWall tramite ID
    public PokeWall getPostById(int postId) throws SystemException {
        List<PokeWall> posts = pokeWallDAO.getAllPosts();
        for (PokeWall pokeWall : posts) {
            if (pokeWall.getId() == postId) {
                return pokeWall;
            }
        }
        return null;
    }
}