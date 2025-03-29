package org.example.ispwproject.utils.enumeration.ingredient;

public enum SaucesAlternative implements GenericAlternative{

    TERIYAKI(2.00, "Sweet, savory, thick, tangy."),
    SOY(1.00, "Salty, umami, dark, fermented."),
    WASABI(1.50, "Spicy, pungent, sharp, green.");

    private final double price;
    private static final String ALTERNATIVE="Sauces";
    private final String description;

    SaucesAlternative(double price, String description){
        this.price= price;
        this.description= description;
    }

    @Override
    public double price(){return price;}

    public String alternative() {return ALTERNATIVE;}

    @Override
    public String description() {return description;}
}
