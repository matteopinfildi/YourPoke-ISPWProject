package org.example.ispwproject.model.decorator;

import org.example.ispwproject.model.Decorator;
import org.example.ispwproject.model.Ingredient;
import org.example.ispwproject.utils.enumeration.Topping;

public class ToppingDecorator extends Decorator {
    private int plid;
    private Topping topping;
    private boolean crispy;

    public ToppingDecorator(Ingredient ingredient){
        super(ingredient);
    }

    public ToppingDecorator(Ingredient ingredient, Topping topping, boolean crispy){
        super(ingredient);
        this.topping = topping;
        this.crispy = crispy;
    }

    public void defineTopping(Topping topping) {this.topping = topping;}

    public int plid(){return plid;}

    protected double putOnTopping(double earlierPrice){
        if (topping != null) {
            if (crispy) {
                return earlierPrice + putOnCrispy();
            } else return earlierPrice + topping.getPrice();
        }else return earlierPrice;
    }

    protected double putOnCrispy(){return topping.getPrice() * 1.0; }

    @Override
    public double price(){
        // invoco price() in Decorator
        double preliminaryResult = super.price();
        preliminaryResult = this.putOnTopping(preliminaryResult);
        return preliminaryResult;
    }
}
