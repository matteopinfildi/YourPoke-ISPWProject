package org.example.ispwproject.utils.enumeration.ingredient;

public enum FruitOption implements GenericOption {

    AVOCADO(2.00, 160),
    MANGO(1.50, 60),
    STRAWBARRIES(1.00, 32);

    private final double price;
    private final int calories;

    FruitOption(double price, int calories){
        this.price = price;
        this.calories = calories;
    }

    @Override
    public double price(){return price;}

    @Override
    public int calories(){return calories;}
}
