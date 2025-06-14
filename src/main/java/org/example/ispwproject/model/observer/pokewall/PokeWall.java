package org.example.ispwproject.model.observer.pokewall;

import org.example.ispwproject.control.PokeWallObserver;
import org.example.ispwproject.model.observer.PokeWallSubject;

import java.util.*;

public class PokeWall implements PokeWallSubject {
    private int id;
    private String pokeName;
    private String size;
    private String username;
    private List<String> ingredients;
    private List<String> seenByUsers = new ArrayList<>();
    private final Set<PokeWallObserver> observers = new HashSet<>(); // Usiamo Set per evitare duplicati

    public PokeWall(int id, String pokeName, String size, String username, List<String> ingredients) {
        this.id = id;
        this.pokeName = Objects.requireNonNull(pokeName, "Poke name cannot be null");
        this.size = size != null && !size.isEmpty() ? size : "Unknown size";
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.ingredients = Objects.requireNonNull(ingredients, "Ingredients list cannot be null");
    }

    // registra l'observer
    @Override
    public void registerObserver(PokeWallObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    // rimuove l'observer
    @Override
    public void removeObserver(PokeWallObserver observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    // notifica tutti gli observer registrati, passando se stesso come subject osservato
    @Override
    public void notifyObservers() {
        for (PokeWallObserver observer : observers) {
            observer.update(this);
        }
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPokeName() {
        return pokeName;
    }

    public void setPokeName(String pokeName) {
        this.pokeName = Objects.requireNonNull(pokeName, "Poke name cannot be null");
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size != null && !size.isEmpty() ? size : "Unknown size";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = Objects.requireNonNull(username, "Username cannot be null");
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = Objects.requireNonNull(ingredients, "Ingredients list cannot be null");
    }


    // indica il metodo setter per lo stato del subject (ovvero il post)
    public void addSeenUser(String username) {
        if (!seenByUsers.contains(username)) {
            seenByUsers.add(username);
        }
    }

    // indica il metodo getter per lo stato del subject (ovvero il post)
    public List<String> getSeenByUsers() {
        return seenByUsers;
    }

    @Override
    public String toString() {
        return "PokeWall{" +
                "id=" + id +
                ", pokeName='" + pokeName + '\'' +
                ", size='" + size + '\'' +
                ", username='" + username + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}