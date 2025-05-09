package org.example.ispwproject.utils.bean;

import org.example.ispwproject.SessionManager;
import org.example.ispwproject.model.decorator.pokelab.PokeLab;
import org.example.ispwproject.utils.enumeration.ingredient.GenericAlternative;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokeLabBean {
    private Map<String, GenericAlternative> ingredients=new HashMap<>(); //sfrutto una hash map per memorizzare tutti gli ingredienti
    private int plId;
    private double price;
    private String pokeName;
    private String bowlSize;
    private List<String> posts = new ArrayList<>();

    public PokeLabBean(){
        plId= SessionManager.getSessionManager().curPokeId();
       setPrice(0);
    }

    public PokeLabBean(PokeLab pokeLab){
        this.plId = pokeLab.id();
        this.price = pokeLab.price();
        this.ingredients =new HashMap<>(pokeLab.allIngredients());
    }

    public String getPokeName() {
        return pokeName;
    }

    public void setPokeName(String pokeName){
        this.pokeName = pokeName;
    }

    public String getBowlSize(){
        return bowlSize;
    }

    public void setBowlSize(String bowlSize) {
        this.bowlSize = bowlSize;
    }

    public GenericAlternative getIngredient(String ingredientName) {return ingredients.get(ingredientName);}

    public void setIngredient(String ingredientName, GenericAlternative alternative) {
        //aggiunge il nuovo ingrediente (ho solo un alternativa per ingrediente)
        ingredients.put(ingredientName, alternative);
    }

    public Map<String, GenericAlternative> getAllIngredients(){return ingredients;}

    public boolean isFull() {return ingredients.size() == 5;} //controllo se ho inserito tutti gli ingredienti

    public double getPrice() {return price;}
    public void setPrice(double price) {this.price = price;}

    public int getId() {return plId;}
    public void setId(int id) {this.plId = id;}


}



