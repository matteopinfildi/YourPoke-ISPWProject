//package org.example.ispwproject.control.application;
//
//import org.example.ispwproject.model.PokeWallObserver;
//import org.example.ispwproject.model.PokeWallSubject;
//import org.example.ispwproject.model.observer.pokewall.PokeWall;
//import org.example.ispwproject.model.observer.pokewall.PokeWallDAO;
//import org.example.ispwproject.utils.bean.PokeWallBean;
//import org.example.ispwproject.utils.bean.SaveBean;
//import org.example.ispwproject.model.user.User;
//import org.example.ispwproject.model.user.UserDAO;
//import org.example.ispwproject.utils.dao.DAOFactory;
//import org.example.ispwproject.utils.exception.SystemException;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PokeWallAppController implements PokeWallSubject {
//
//    private final PokeWallDAO pokeWallDAO;
//    private final UserDAO userDAO;
//    private final List<PokeWallObserver> observers = new ArrayList<>();
//    private static final PokeWallAppController instance = new PokeWallAppController();
//
//    public static PokeWallAppController getInstance() {
//        return instance;
//    }
//
//
//    public PokeWallAppController() {
//        DAOFactory daoFactory = DAOFactory.getInstance();
//        this.pokeWallDAO = daoFactory.getPokeWallDAO(); // <--- usa la factory!
//        this.userDAO = daoFactory.getUserDAO();
//    }
//
//    @Override
//    public void registerObserver(PokeWallObserver observer) {
//        observers.add(observer);
//    }
//
//    @Override
//    public void removeObserver(PokeWallObserver observer) {
//        observers.remove(observer);
//    }
//
//    @Override
//    public void notifyObservers(PokeWall newPost) {
//        for (PokeWallObserver observer : observers) {
//            observer.update(newPost);
//        }
//    }
//
//    public boolean createPost(SaveBean saveBean, PokeWallBean pokeWallBean) throws SystemException {
//        String userId = saveBean.getUid();
//        User user = userDAO.read(userId);
//        if (user == null) {
//            System.out.println("User not found!");
//            return false;
//        }
//
//        PokeWall pokeWall = new PokeWall(
//                pokeWallBean.getPokeName(),
//                pokeWallBean.getSize(),
//                user.getUsername(),
//                pokeWallBean.getIngredients()
//        );
//
//        pokeWallDAO.create(pokeWall);
//        notifyObservers(pokeWall);
//        return true;
//    }
//
//    public List<PokeWall> getAllPosts() throws SystemException {
//        List<PokeWall> posts = pokeWallDAO.getAllPosts();
//        if (posts == null || posts.isEmpty()) {
//            System.out.println("No posts returned from DAO!");
//        }
//        return posts;
//    }
//
//    public boolean deletePost(int postId, String requestingUsername) throws SystemException {
//        try {
//            PokeWall postToDelete = getPostById(postId);
//            if (postToDelete == null) {
//                System.out.println("Post not found!");
//                return false;
//            }
//
//            if (!postToDelete.getUsername().equals(requestingUsername)) {
//                System.out.println("User is not authorized to delete this post!");
//                return false;
//            }
//
//            pokeWallDAO.delete(postId);
//            System.out.println("Post deleted successfully!");
//            return true;
//        } catch (SystemException e) {
//            System.out.println("Failed to delete post: " + e.getMessage());
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
//}
package org.example.ispwproject.control.application;

import org.example.ispwproject.model.PokeWallObserver;
import org.example.ispwproject.model.PokeWallSubject;
import org.example.ispwproject.model.observer.pokewall.PokeWall;
import org.example.ispwproject.model.observer.pokewall.PokeWallDAO;
import org.example.ispwproject.utils.bean.PokeWallBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.model.user.User;
import org.example.ispwproject.model.user.UserDAO;
import org.example.ispwproject.utils.dao.DAOFactory;
import org.example.ispwproject.utils.exception.SystemException;

import java.util.ArrayList;
import java.util.List;

public class PokeWallAppController implements PokeWallSubject {

    private final PokeWallDAO pokeWallDAO;
    private final UserDAO userDAO;
    private final List<PokeWallObserver> observers = new ArrayList<>();
    private static final PokeWallAppController instance = new PokeWallAppController();

    private PokeWallAppController() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        this.pokeWallDAO = daoFactory.getPokeWallDAO(); // <--- usa la factory!
        this.userDAO = daoFactory.getUserDAO();
    }

    public static PokeWallAppController getInstance() {
        return instance;
    }

    @Override
    public void registerObserver(PokeWallObserver observer) {
        observers.add(observer);
        // appena un observer si registra, lo aggiorniamo con tutti i post esistenti
        try {
            List<PokeWall> allPosts = getAllPosts();
            for (PokeWall post : allPosts) {
                observer.update(post);
            }
        } catch (SystemException e) {
            System.out.println("Errore nel recuperare i post per observer: " + e.getMessage());
        }
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

        PokeWall pokeWall = new PokeWall(
                pokeWallBean.getPokeName(),
                pokeWallBean.getSize(),
                user.getUsername(),
                pokeWallBean.getIngredients()
        );

        pokeWallDAO.create(pokeWall);
        notifyObservers(pokeWall);
        return true;
    }

    public List<PokeWall> getAllPosts() throws SystemException {
        List<PokeWall> posts = pokeWallDAO.getAllPosts();
        if (posts == null || posts.isEmpty()) {
            System.out.println("No posts returned from DAO!");
        }
        return posts;
    }

    public boolean deletePost(int postId, String requestingUsername) throws SystemException {
        try {
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
}
