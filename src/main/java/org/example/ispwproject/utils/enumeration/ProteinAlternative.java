package org.example.ispwproject.utils.enumeration;

public enum ProteinAlternative implements GenericAlternative{

    SALMON(4.00, "Poi lo scrivo!"),
    SHRIMP(3.00, "Poi lo scrivo!"),
    TUNA(3.50, "Poi lo scrivo!");

    private final double price;
    private static final String ALTERNATIVE="Protein";
    private final String description;

    ProteinAlternative(double price, String description){
        this.price= price;
        this.description= description;
    }

    @Override
    public double price(){return price;}

    public String alternative() {return ALTERNATIVE;}

    @Override
    public String description() {return description;}
}
