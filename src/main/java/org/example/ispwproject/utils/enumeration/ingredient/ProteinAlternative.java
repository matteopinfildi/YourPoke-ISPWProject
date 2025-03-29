package org.example.ispwproject.utils.enumeration.ingredient;

public enum ProteinAlternative implements GenericAlternative{

    SALMON(4.00, "Rich, oily, tender, flavorful."),
    SHRIMP(3.00, "Sweet, tender, succulent, versatile."),
    TUNA(3.50, "Lean, firm, mild, protein-packed.");

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
