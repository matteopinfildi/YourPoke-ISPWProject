package org.example.ispwproject.model.decorator.topping;

import java.util.Collection;

public interface ToppingDAO {
    void create(ToppingDecorator toppingDecorator);

    Collection<ToppingDecorator> read(int plid);
}
