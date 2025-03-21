package org.example.ispwproject.model.decorator;

import org.example.ispwproject.model.Decorator;
import org.example.ispwproject.model.Ingredient;

public class SpicyDecorator extends Decorator {
    private double[] spicyQuantity = new double[2];
    private String spicy;

    public SpicyDecorator(Ingredient ingredient, String spicy){
        super(ingredient);
        this.spicy = spicy;
    }

    protected double putOnSpicy(double extraPrice){
        double newExtraPrice = extraPrice + 0.5;
        return Math.round(newExtraPrice * 100.0) / 100.0;
    }

    @Override
    public double price() {
        double preliminaryResult = super.price();
        preliminaryResult = this.putOnSpicy(preliminaryResult);
        return preliminaryResult;
    }
}
