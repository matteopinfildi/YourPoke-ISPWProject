package org.example.ispwproject.utils.bean;

import org.example.ispwproject.utils.exception.SystemException;

import java.util.List;

public class PokeWallBean {
    private String pokeName;
    private String size;
    private String username;
    private List<String> ingredients;


    public PokeWallBean() {
        // Costruttore vuoto
    }


    // Getter e Setter
    public String getPokeName() { return pokeName; }
    public void setPokeName(String pokeName) throws SystemException{
        if (pokeName == null || pokeName.trim().length() < 4) {
            throw new SystemException("Il nome del poke deve essere lungo almeno 4 caratteri!");
        }
        this.pokeName = pokeName;
    }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }
}