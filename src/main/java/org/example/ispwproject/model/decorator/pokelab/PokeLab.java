package org.example.ispwproject.model.decorator.pokelab;

import org.example.ispwproject.model.Ingredient;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.enumeration.ingredient.GenericAlternative;

import java.util.HashMap;
import java.util.Map;

public class PokeLab extends Ingredient {

    private int id;
    private double totalPrice;

    private Map<String, GenericAlternative> items=new HashMap<>();
    private String bowlSize;

    public PokeLab(double price) {this.totalPrice = price;}

    public PokeLab(PokeLabBean pokeLab){
        this.id = pokeLab.getId();
        this.totalPrice = pokeLab.getPrice();
        this.items = new HashMap<>(pokeLab.getAllIngredients());
    }

    public PokeLab(double price, int id, Map<String, GenericAlternative> items, String bowlSize) {
        this.totalPrice = price;
        this.id = id;
        this.items = items;
        this.bowlSize = bowlSize;
    }

    @Override
    public double price(){
        return this.totalPrice;
    }

    public int id(){return this.id;}

    public Map<String, GenericAlternative> allIngredients(){return items;}

    public String getBowlSize() {
        return bowlSize;
    }

    public void setBowlSize(String bowlSize) {
        this.bowlSize = bowlSize;
    }

}
