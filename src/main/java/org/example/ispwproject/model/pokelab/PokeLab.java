package org.example.ispwproject.model.pokelab;

import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.enumeration.ingredient.GenericAlternative;

import java.util.HashMap;
import java.util.Map;

public class PokeLab  {

    private int id;

    private Map<String, GenericAlternative> items=new HashMap<>();
    private String bowlSize;


    public PokeLab(PokeLabBean pokeLab){
        this.id = pokeLab.getId();;
        this.items = new HashMap<>(pokeLab.getAllIngredients());
        this.bowlSize = pokeLab.getBowlSize();
    }

    public PokeLab(double price, int id, Map<String, GenericAlternative> items, String bowlSize) {
        this.id = id;
        this.items = items;
        this.bowlSize = bowlSize;
    }

    public double price() {
        return items.values().stream()
                .mapToDouble(GenericAlternative::price).sum();
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
        if(id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
        this.id = id;
    }
}
