package org.example.ispwproject.utils.enumeration.ingredient;

public enum CrunchyAlternative implements GenericAlternative{

    ONION(2.00, "Poi lo scrivo!"),
    NUTS(1.00, "Poi lo scrivo!"),
    ALMONDS(1.50, "Poi lo scrivo!");

    private final double price;
    private static final String ALTERNATIVE="Crunchy";
    private final String description;

    CrunchyAlternative(double price, String description){
        this.price= price;
        this.description= description;
    }

    @Override
    public double price(){return price;}

    public String alternative() {return ALTERNATIVE;}

    @Override
    public String description() {return description;}
}
