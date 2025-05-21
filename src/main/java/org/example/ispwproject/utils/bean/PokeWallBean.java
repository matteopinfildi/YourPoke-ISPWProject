package org.example.ispwproject.utils.bean;

import org.example.ispwproject.model.observer.pokewall.PokeWall;

import java.util.List;

public class PokeWallBean {
    private String pokeName;
    private String size;
    private String username;
    private List<String> ingredients;

    // Costruttore vuoto
    public PokeWallBean() {}


    // Getter e Setter
    public String getPokeName() { return pokeName; }
    public void setPokeName(String pokeName) { this.pokeName = pokeName; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }
}