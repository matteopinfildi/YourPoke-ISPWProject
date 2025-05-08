package org.example.ispwproject.model;


public interface PokeWallSubject {
    void registerObserver(PokeWallObserver observer);
    void removeObserver(PokeWallObserver observer);
    void notifyObservers();
}
