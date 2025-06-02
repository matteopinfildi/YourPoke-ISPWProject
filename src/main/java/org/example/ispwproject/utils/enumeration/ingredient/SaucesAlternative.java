package org.example.ispwproject.utils.enumeration.ingredient;

public enum SaucesAlternative implements GenericAlternative{

    TERIYAKI(2.00, 450),
    SOY(1.00, 600),
    WASABI(1.50, 580);

    private final double price;
    private final int calories;

    SaucesAlternative(double price, int calories){
        this.price = price;
        this.calories = calories;
    }

    @Override
    public double price(){return price;}

    @Override
    public int calories() {return calories;}

}
