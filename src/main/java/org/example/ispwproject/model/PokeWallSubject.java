package org.example.ispwproject.model;


import org.example.ispwproject.control.PokeWallObserver;

public interface PokeWallSubject {
    void registerObserver(PokeWallObserver observer);
    void removeObserver(PokeWallObserver observer);
    void notifyObservers();
}
