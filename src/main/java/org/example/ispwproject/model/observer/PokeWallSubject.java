package org.example.ispwproject.model.observer;


import org.example.ispwproject.control.PokeWallObserver;

public interface PokeWallSubject {
    void registerObserver(PokeWallObserver observer);
    void removeObserver(PokeWallObserver observer);
    void notifyObservers();
}
