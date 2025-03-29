package org.example.ispwproject.utils.enumeration.ingredient;

public enum RiceAlternative implements GenericAlternative {

    SUSHI(3.00, "Sticky, vinegary, tender, flavorful."),
    VENUS(4.00, "Nutty, aromatic, black, nutritious."),
    BASMATI(3.50, "Fragrant, long-grain, fluffy, aromatic.");

    private final double price;
    private static final String ALTERNATIVE="Rice";
    private final String description;

    RiceAlternative(double price, String description){
        this.price= price;
        this.description= description;
    }

    @Override
    public double price(){return price;}

    public String alternative() {return ALTERNATIVE;}

    @Override
    public String description() {return description;}
}

