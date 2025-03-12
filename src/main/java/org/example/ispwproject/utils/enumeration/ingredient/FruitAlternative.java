package org.example.ispwproject.utils.enumeration.ingredient;

public enum FruitAlternative implements GenericAlternative{

    AVOCADO(2.00, "Poi lo scrivo!"),
    MANGO(1.50, "Poi lo scrivo!"),
    STRAWBARRIES(1.00, "Poi lo scrivo!");

    private final double price;
    private static final String ALTERNATIVE="Fruit";
    private final String description;

    FruitAlternative(double price, String description){
        this.price= price;
        this.description= description;
    }

    @Override
    public double price(){return price;}

    public String alternative() {return ALTERNATIVE;}

    @Override
    public String description() {return description;}
}
