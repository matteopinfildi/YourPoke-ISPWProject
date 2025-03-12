package org.example.ispwproject.utils.enumeration;

public enum ExtraIngredient {
    OCTOPUS(2.00),
    PINEAPPLE(1.50),
    NACHOS(1.50),
    MAYO(1.00);

    private final double price;

    ExtraIngredient(double price) {this.price= price;}

    public double getPrice() {return price;}
}
