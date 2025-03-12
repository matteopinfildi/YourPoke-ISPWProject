package org.example.ispwproject.utils.enumeration.ingredient;

public enum SaucesAlternative implements GenericAlternative{

    TERIYAKI(2.00, "Poi lo scrivo!"),
    SOY(1.00, "Poi lo scrivo!"),
    WASABI(1.50, "Poi lo scrivo!");

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
