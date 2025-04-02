package org.example.ispwproject.utils.bean;

import java.util.Map;

public class ExtraBean {

    private int id;
    private boolean[][] topping; // da valutare se usare un array
    private Map<String, Integer> spicyMap;

    public ExtraBean(boolean[][] toppings, Map<String, Integer> spicy){
        this.topping = toppings;
        this.spicyMap = spicy;
    }

    public int getId() {return id;}

    public boolean[][]  getTopping() {return topping;}

    public Map<String, Integer> getSpicyMap() {return spicyMap;}

}
