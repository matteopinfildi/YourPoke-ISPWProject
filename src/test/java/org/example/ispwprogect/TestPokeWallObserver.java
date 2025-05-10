package org.example.ispwprogect;

import org.example.ispwproject.model.PokeWallObserver;
import org.example.ispwproject.model.observer.pokewall.PokeWall;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// @author Matteo Pinfildi

class TestPokeWallObserver {

    @Test
    void testBasicObserverPattern() {
        // 1. Setup
        List<String> ingredients = List.of("rice", "tuna", "avocado");
        PokeWall pokeWall = new PokeWall(1, "Tuna Poke", "Medium", "user1", ingredients);

        // Mock observer usando una semplice classe interna
        class TestObserver implements PokeWallObserver {
            public int updateCount = 0;
            public PokeWall lastReceived;

            @Override
            public void update(PokeWall pokeWall) {
                updateCount++;
                lastReceived = pokeWall;
            }
        }

        TestObserver observer = new TestObserver();

        // 2. Esecuzione
        pokeWall.registerObserver(observer);
        pokeWall.notifyObservers();

        // 3. Verifiche
        assertEquals(1, observer.updateCount, "L'observer dovrebbe essere notificato una volta");
        assertSame(pokeWall, observer.lastReceived, "L'observer dovrebbe ricevere l'oggetto PokeWall corretto");

        // Pulizia
        pokeWall.removeObserver(observer);
        pokeWall.notifyObservers();

        assertEquals(1, observer.updateCount, "L'observer non dovrebbe più ricevere notifiche dopo la rimozione");
    }
}