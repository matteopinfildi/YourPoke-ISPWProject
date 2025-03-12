package org.example.ispwproject.utils.enumeration;

public enum RiceAlternative implements GenericAlternative {

    SUSHI(3.00, "Poi lo scrivo!"),
    VENUS(4.00, "Poi lo scrivo!"),
    BASMATI(3.50, "Poi lo scrivo!");

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

