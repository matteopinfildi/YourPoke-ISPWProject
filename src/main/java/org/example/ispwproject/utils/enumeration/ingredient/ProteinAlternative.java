package org.example.ispwproject.utils.enumeration.ingredient;

public enum ProteinAlternative implements GenericAlternative{

    SALMON(4.00, 208),
    SHRIMP(3.00, 85),
    TUNA(3.50, 144);

    private final double price;
    private final int calories;

    ProteinAlternative(double price, int calories){
        this.price = price;
        this.calories = calories;
    }

    @Override
    public double price(){return price;}

    @Override
    public int calories(){return calories;}
}
