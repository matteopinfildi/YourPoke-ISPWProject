package org.example.ispwproject.utils.enumeration;

public enum Topping {
    EDEMAME(2.00),
    CUCUMBER(1.00),
    CARROT(1.50);

    private final double price;

    Topping(double price) {this.price= price;}

    public double getPrice() {return price;}
}
