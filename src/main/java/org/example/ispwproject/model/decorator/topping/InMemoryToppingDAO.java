package org.example.ispwproject.model.decorator.topping;

import java.util.ArrayList;
import java.util.Collection;

public class InMemoryToppingDAO implements ToppingDAO{
    private static InMemoryToppingDAO instance;

    private InMemoryToppingDAO(){}

    public static InMemoryToppingDAO getInstance(){
        if (instance == null) {
            instance = new InMemoryToppingDAO();
        }
        return instance;
    }

    private Collection<ToppingDecorator> toppings = new ArrayList<>();

    @Override
    public void create (ToppingDecorator toppingDecorator) {toppings.add(toppingDecorator);}

    @Override
    public Collection<ToppingDecorator> read(int plid) {
        Collection<ToppingDecorator> selectedTopping = new ArrayList<>();
        for (ToppingDecorator toppingDecorator : toppings) {
            if (toppingDecorator.plid() == plid){
                selectedTopping.add(toppingDecorator);
            }
        }
        return selectedTopping;
    }
}
