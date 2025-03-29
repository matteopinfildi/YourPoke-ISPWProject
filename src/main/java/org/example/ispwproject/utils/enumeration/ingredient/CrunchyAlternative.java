package org.example.ispwproject.utils.enumeration.ingredient;

public enum CrunchyAlternative implements GenericAlternative{

    ONION(2.00, "Pungent, layered, aromatic, versatile."),
    NUTS(1.00, "Crunchy, nutritious, energy-dense, varied."),
    ALMONDS(1.50, "Crunchy, nutty, healthy, protein-rich.");

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
