package org.example.ispwproject.model;

import org.example.ispwproject.model.observer.pokewall.PokeWall;

public interface PokeWallSubject {
    void registerObserver(PokeWallObserver observer);
    void removeObserver(PokeWallObserver observer);
    void notifyObservers(PokeWall newPost);
}
