package org.example.ispwproject.model.observer.pokewall;

import org.example.ispwproject.utils.exception.SystemException;

import java.util.List;

public interface PokeWallDAO {
    void create(PokeWall pokeWall) throws SystemException;
    List<PokeWall> getAllPosts() throws SystemException;
    void delete(int postId) throws SystemException;
    // restituisce i post non ancora visualizzati da un determinato utente
    List<PokeWall> getUnseenPosts(String username) throws SystemException;
    // marca i post visti dall'utente come visualizzati, associando id del post e username dell'utente
    void markPostAsSeen(int postId, String username) throws SystemException;
}