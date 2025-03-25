package org.example.ispwproject.model.pokewall;

import java.time.LocalDateTime;
import java.util.List;

public class PokeWall {
    private static int counter = 0;  // Counter per generare ID univoci
    private int id;  // Identificatore univoco
    private String content;
    private String username;
    private List<String> ingredients;

    public PokeWall(String content, String username, List<String> ingredients) {
        this.id = counter++; // genero un ID univoco, incrementando il counter
        this.content = content;
        this.username = username;
        this.ingredients = ingredients;
    }


    public int getId() {
        return id;
    }

    public void setId (int id){
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getIngredients() { return ingredients; }
}
