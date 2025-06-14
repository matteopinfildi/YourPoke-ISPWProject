package org.example.ispwproject.utils.bean;

import org.example.ispwproject.SessionManager;
import org.example.ispwproject.model.pokelab.PokeLab;
import org.example.ispwproject.utils.enumeration.ingredient.GenericOption;
import org.example.ispwproject.utils.exception.SystemException;

import java.util.HashMap;
import java.util.Map;

public class PokeLabBean {
    private Map<String, GenericOption> ingredients=new HashMap<>(); // hash map per memorizzare tutti gli ingredienti
    private int plId;
    private double price;
    private int calories;
    private String pokeName;
    private String bowlSize;

    public PokeLabBean(){
        plId= SessionManager.getSessionManager().curPokeId();
       setPrice(0);
    }

    public PokeLabBean(PokeLab pokeLab){
        this.plId = pokeLab.id();
        this.price = pokeLab.price();
        this.calories = pokeLab.calories();
        this.ingredients =new HashMap<>(pokeLab.allIngredients());
    }

    public String getPokeName() {
        return pokeName;
    }

    public void setPokeName(String pokeName) throws SystemException{
        if (pokeName == null || pokeName.trim().length() < 4) {
            throw new SystemException("The name must be at least 4 characters long!");
        }
        this.pokeName = pokeName;
    }

    public String getBowlSize(){
        return bowlSize;
    }

    public void setBowlSize(String bowlSize) throws SystemException{
        if (bowlSize == null || bowlSize.trim().isEmpty()) {
            throw new SystemException("Bowl size cannot be empty");
        }
        this.bowlSize = bowlSize;
    }

    public boolean isBowlSizeSelected() {
        return bowlSize != null;
    }



    public GenericOption getIngredient(String ingredientName) {return ingredients.get(ingredientName);}

    // aggiunge un nuovo ingrediente
    public void setIngredient(String ingredientName, GenericOption option) {
        ingredients.put(ingredientName, option);
    }

    public Map<String, GenericOption> getAllIngredients(){return ingredients;}


    public double getPrice() {return price;}
    public void setPrice(double price) {this.price = price;}

    public int getCalories() {return calories;}
    public void setCalories(int calories) {this.calories = calories;}

    public int getId() {return plId;}
    public void setId(int id) {this.plId = id;}

    private boolean hasIngredient(String type) {
        return getIngredient(type) != null;
    }

    public boolean isComplete() {
        return hasIngredient("rice") &&
                hasIngredient("protein") &&
                hasIngredient("fruit") &&
                hasIngredient("crunchy") &&
                hasIngredient("sauces");
    }

}



