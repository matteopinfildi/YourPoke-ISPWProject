package org.example.ispwproject.utils.enumeration.ingredient;

public enum RiceAlternative implements GenericAlternative {

    SUSHI(3.00, 340),
    VENUS(4.00, 360),
    BASMATI(3.50, 350);

    private final double price;
    private final int calories;

    RiceAlternative(double price, int calories){
        this.price = price;
        this.calories = calories;
    }

    @Override
    public double price(){return price;}

    @Override
    public int calories(){return calories;}
}

