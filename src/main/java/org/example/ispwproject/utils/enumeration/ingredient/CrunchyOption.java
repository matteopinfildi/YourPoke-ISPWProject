package org.example.ispwproject.utils.enumeration.ingredient;

public enum CrunchyOption implements GenericOption {

    ONION(2.00, 450),
    NUTS(1.00, 600),
    ALMONDS(1.50, 580);

    private final double price;
    private final int calories;

    CrunchyOption(double price, int calories){
        this.price = price;
        this.calories = calories;
    }

    @Override
    public double price(){return price;}

    @Override
    public int calories(){return calories;}
}
