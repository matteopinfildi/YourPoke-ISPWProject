//package org.example.ispwproject.model.observer.pokewall;
//
//import java.util.List;
//
//public class PokeWall {
//    private int id;  // Identificatore univoco
//    private String pokeName;
//    private String size;
//    private String username;
//    private List<String> ingredients;
//
//    public PokeWall(String pokeName, String size, String username, List<String> ingredients) {
//        this.pokeName = pokeName;
//        this.size = size != null && !size.isEmpty() ? size : "Unknown size";
//        this.username = username;
//        this.ingredients = ingredients;
//    }
//
//    // Getter e Setter
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getPokeName() {
//        return pokeName;
//    }
//
//    public void setPokeName(String pokeName) {
//        this.pokeName = pokeName;
//    }
//
//    public String getSize(){return size;}
//
//    public void setSize(String size) {this.size = size  != null && !size.isEmpty() ? size : "Unknown size";}
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public List<String> getIngredients() { return ingredients; }
//}

package org.example.ispwproject.model.observer.pokewall;

import java.util.List;
import java.util.Objects;

public class PokeWall {
    private int id;
    private String pokeName;
    private String size;
    private String username;
    private List<String> ingredients;

    public PokeWall(String pokeName, String size, String username, List<String> ingredients) {
        this.pokeName = Objects.requireNonNull(pokeName, "Poke name cannot be null");
        this.size = size != null && !size.isEmpty() ? size : "Unknown size";
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.ingredients = Objects.requireNonNull(ingredients, "Ingredients list cannot be null");
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