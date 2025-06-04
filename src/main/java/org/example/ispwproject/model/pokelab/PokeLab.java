package org.example.ispwproject.model.pokelab;

import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.enumeration.ingredient.GenericAlternative;

import java.util.HashMap;
import java.util.Map;

public class PokeLab  {

    private int id;
    private double price;
    private int calories;
    private Map<String, GenericAlternative> items = new HashMap<>();
    private String bowlSize;


    public PokeLab(PokeLabBean pokeLab){
        this.id = pokeLab.getId();
        this.items = new HashMap<>(pokeLab.getAllIngredients());
        this.bowlSize = pokeLab.getBowlSize();
        this.price = pokeLab.getPrice();
        this.calories = pokeLab.getCalories();
    }

    public PokeLab(double price, int id, Map<String, GenericAlternative> items, String bowlSize, int calories) {
        this.price = price;
        this.id = id;
        this.items = items;
        this.bowlSize = bowlSize;
        this.calories = calories;
    }

    // restituisce la somma di tutti i prezzi degli ingredienti di un poke lab
    public double price() {
        return items.values().stream().mapToDouble(GenericAlternative::price).sum();
    }

    // restituisce la somma di tutte le calorie degli ingredienti di un poke lab
    public int calories(){
        return items.values().stream().mapToInt(GenericAlternative::calories).sum();
    }

    public int id(){return this.id;}

    public Map<String, GenericAlternative> allIngredients(){return items;}

    public String getBowlSize() {
        return bowlSize;
    }

    public void setBowlSize(String bowlSize) {
        this.bowlSize = bowlSize;
    }

    public void setId(int id) {
        // controllo che l'id non sia negativo
        if(id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
        this.id = id;
    }
}
