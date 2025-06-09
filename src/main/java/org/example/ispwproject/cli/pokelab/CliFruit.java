package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.FruitOption;
import java.util.List;

public class CliFruit extends CliIngredient<FruitOption> {
    public CliFruit() {
        super("fruit", List.of(
                        FruitOption.AVOCADO,
                        FruitOption.MANGO,
                        FruitOption.STRAWBARRIES),
                List.of("Avocado: 2.00 $, 160 cal",
                        "Mango: 1.50 $, 60 cal",
                        "Strawbarries: 1.00 $, 32 cal"));
    }
}
