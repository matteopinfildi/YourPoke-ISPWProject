package org.example.ispwproject.model;

public class Decorator extends  Ingredient{

    private Ingredient ingredient;

    //Questo vuol dire che stiamo decorando Ingredient, ma io voglio decorare pure bowl
    // o cambio logica o aggiungo una classe Bowl da decorare
    // forse arrivati a questo punto è meglio cambiare logica!!!
    protected Decorator(Ingredient ingredient){this.ingredient = ingredient;}

}
