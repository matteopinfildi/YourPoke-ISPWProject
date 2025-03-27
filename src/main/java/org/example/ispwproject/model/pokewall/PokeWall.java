package org.example.ispwproject.model.pokewall;

import java.util.List;

public class PokeWall {
    private int id;  // Identificatore univoco
    private String pokeName;
    private String size;
    private String username;
    private List<String> ingredients;

    public PokeWall(String pokeName, String size, String username, List<String> ingredients) {
        this.pokeName = pokeName;
        this.size = size;
        this.username = username;
        this.ingredients = ingredients;
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
        this.pokeName = pokeName;
    }

    public String getSize(){return size;}

    public void setSize(String size) {this.size = size;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getIngredients() { return ingredients; }
}