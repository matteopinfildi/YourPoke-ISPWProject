package org.example.ispwproject.utils.bean;

import org.example.ispwproject.utils.enumeration.ingredient.GenericAlternative;

public class AddIngredientBean {
    private String ingredientName;
    private GenericAlternative genericAlternative;

    public AddIngredientBean(String ingredientName, GenericAlternative genericAlternative){
        this.ingredientName = ingredientName;
        this.genericAlternative = genericAlternative;
    }

    public String getIngredientName() {return ingredientName;}
    public GenericAlternative getGenericAlternative() {return genericAlternative;}
}
