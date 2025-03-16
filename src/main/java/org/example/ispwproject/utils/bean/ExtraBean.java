package org.example.ispwproject.utils.bean;

import java.util.HashMap;

public class ExtraBean {

    private int id;
    private String topping; // da valutare se usare un array
    private HashMap<String, Integer> spicyMap;

    public ExtraBean(String toppings, HashMap<String, Integer> spicy){
        this.topping = toppings;
        this.spicyMap = spicy;
    }

    public int getId() {return id;}

    public String getTopping() {return topping;} // da vedere

    public HashMap<String, Integer> getSpicyMap() {return spicyMap;}

}
