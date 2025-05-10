package org.example.ispwproject.model.observer.pokewall;

import org.example.ispwproject.utils.exception.SystemException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryPokeWallDAO implements PokeWallDAO {
    private static InMemoryPokeWallDAO instance;
    private List<PokeWall> posts = new ArrayList<>();
    private static int lastId = 0;

    private InMemoryPokeWallDAO() {}

    public static synchronized InMemoryPokeWallDAO getInstance() {
        if (instance == null) {
            instance = new InMemoryPokeWallDAO();
        }
        return instance;
    }

    @Override
    public void create(PokeWall pokeWall) throws SystemException {
        synchronized (InMemoryPokeWallDAO.class) {
            // Assicurati che l'ID sia univoco e incrementale
            if (pokeWall.getId() <= lastId) {
                lastId++;
                pokeWall.setId(lastId);
            } else {
                lastId = pokeWall.getId();
            }
        }
        posts.add(pokeWall);
    }

    @Override
    public List<PokeWall> getAllPosts() throws SystemException {
        return new ArrayList<>(posts);
    }

    @Override
    public void delete(int postId) throws SystemException {
        posts.removeIf(post -> post.getId() == postId);
    }

    @Override
    public List<PokeWall> getUnseenPosts(String username) throws SystemException {
        // Filtra i post che l'utente non ha ancora visto (ipotesi: implementare un attributo 'seen' nel PokeWall)
        return posts.stream()
                .filter(post -> !post.getSeenByUsers().contains(username))
                .toList();
    }

    @Override
    public void markPostAsSeen(int postId, String username) throws SystemException {
        // Trova il post e aggiungi l'username alla lista degli utenti che lo hanno visto
        PokeWall post = posts.stream()
                .filter(p -> p.getId() == postId)
                .findFirst()
                .orElseThrow(() -> new SystemException("Post not found"));
        post.addSeenUser(username);
    }
}
