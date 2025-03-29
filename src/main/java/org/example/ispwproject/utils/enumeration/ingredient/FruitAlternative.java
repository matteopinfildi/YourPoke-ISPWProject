package org.example.ispwproject.utils.enumeration.ingredient;

public enum FruitAlternative implements GenericAlternative{

    AVOCADO(2.00, "Creamy, nutritious, versatile fruit."),
    MANGO(1.50, "Sweet, tropical, juicy, vibrant."),
    STRAWBARRIES(1.00, "Red, juicy, sweet, refreshing.");

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
