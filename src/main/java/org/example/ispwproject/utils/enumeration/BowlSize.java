package org.example.ispwproject.utils.enumeration;

public enum BowlSize {
    SMALL(2.50),
    MEDIUM(3.50),
    LARGE(4.00);

    private final double price;

    BowlSize(double price) {this.price= price;}

    public double getPrice() {return price;}
}
