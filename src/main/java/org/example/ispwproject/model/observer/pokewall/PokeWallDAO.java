package org.example.ispwproject.model.observer.pokewall;

import org.example.ispwproject.utils.exception.SystemException;

import java.util.List;

public interface PokeWallDAO {
    void create(PokeWall pokeWall) throws SystemException;
    List<PokeWall> getAllPosts() throws SystemException;
    void delete(int postId) throws SystemException;
}