package org.example.ispwproject.utils.bean;

import java.util.HashMap;

public class ExtraBean {

    private int id;
    private boolean[][] topping; // da valutare se usare un array
    private HashMap<String, Integer> spicyMap;

    public ExtraBean(boolean[][] toppings, HashMap<String, Integer> spicy){
        this.topping = toppings;
        this.spicyMap = spicy;
    }

    public int getId() {return id;}

    public boolean[][]  getTopping() {return topping;}

    public HashMap<String, Integer> getSpicyMap() {return spicyMap;}

}
