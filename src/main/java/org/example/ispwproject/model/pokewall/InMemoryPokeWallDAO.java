package org.example.ispwproject.model.pokewall;

import java.util.ArrayList;
import java.util.List;

public class InMemoryPokeWallDAO implements PokeWallDAO {
    private static InMemoryPokeWallDAO instance;
    private List<PokeWall> posts = new ArrayList<>();

    private InMemoryPokeWallDAO() {}

    public static synchronized InMemoryPokeWallDAO getInstance() {
        if (instance == null) {
            instance = new InMemoryPokeWallDAO();
        }
        return instance;
    }

    @Override
    public void create(PokeWall pokeWall) {
        pokeWall = new PokeWall(pokeWall.getPokeName(), pokeWall.getSize(), pokeWall.getUsername(), pokeWall.getIngredients());
        posts.add(pokeWall);
    }

    @Override
    public List<PokeWall> getAllPosts() {
        return new ArrayList<>(posts);
    }

    @Override
    public void delete(int postId) {
        posts.removeIf(post -> post.getId() == postId);
    }
}