package org.example.ispwproject.utils.bean;

import org.example.ispwproject.utils.enumeration.ingredient.GenericOption;

public class AddIngredientBean {
    private String ingredientName;
    private GenericOption genericOption;

    public AddIngredientBean(String ingredientName, GenericOption genericOption){
        this.ingredientName = ingredientName;
        this.genericOption = genericOption;
    }

    public String getIngredientName() {return ingredientName;}
    public GenericOption getGenericAlternative() {return genericOption;}
}
