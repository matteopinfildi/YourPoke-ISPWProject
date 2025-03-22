package org.example.ispwproject.model.decorator.spicy;

import java.util.ArrayList;
import java.util.Collection;

public class InMemorySpicyDAO implements SpicyDAO{
    private static InMemorySpicyDAO instance;

    private InMemorySpicyDAO(){}

    public static InMemorySpicyDAO getInstance(){
        if (instance == null) {
            instance = new InMemorySpicyDAO();
        }
        return instance;
    }

    private Collection<SpicyDecorator> spicy = new ArrayList<>();

    @Override
    public void create(SpicyDecorator spicyDecorator) {spicy.add(spicyDecorator);}
}
