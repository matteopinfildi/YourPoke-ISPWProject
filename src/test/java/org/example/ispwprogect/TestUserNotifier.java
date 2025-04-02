package org.example.ispwprogect;

import org.example.ispwproject.model.observer.UserNotifier;
import org.example.ispwproject.model.pokewall.PokeWall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestUserNotifier {

    private UserNotifier userNotifier;
    private PokeWall pokeWall;

    @BeforeEach
    public void setUp() {
        // Creiamo un PokeWall con un pokeName, size, username e una lista di ingredienti
        pokeWall = new PokeWall("SuperPoke", "Medium", "MarioRossi", Arrays.asList("Soy", "Nuts", "Salmon", "Avocado", "Rice"));
        userNotifier = new UserNotifier();
    }

    @Test
    public void testUpdate_NotifiesUser() {
        // Verifica che il metodo update funzioni correttamente
        // Qui non c'è un vero e proprio output da verificare, perciò potresti utilizzare System.out per stampare un messaggio
        userNotifier.update(pokeWall);
        // Non possiamo verificare direttamente il comportamento di System.out, ma possiamo verificare che il metodo non lanci eccezioni
        assertDoesNotThrow(() -> userNotifier.update(pokeWall));
    }

    @Test
    public void testUpdate_WithNullPokeWall() {
        // Simula il caso in cui viene passato un oggetto nullo
        assertThrows(NullPointerException.class, () -> userNotifier.update(null),
                "Dovrebbe lanciare NullPointerException se il PokeWall è nullo.");
    }

    @Test
    public void testUpdate_WithEmptyUsername() {
        // Configura il PokeWall con un username vuoto
        pokeWall = new PokeWall("SuperPoke", "Medium", "", Arrays.asList("Salmon", "Avocado", "Rice"));
        userNotifier.update(pokeWall);
        assertDoesNotThrow(() -> userNotifier.update(pokeWall));
    }
}
