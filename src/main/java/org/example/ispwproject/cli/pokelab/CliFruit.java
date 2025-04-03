package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.FruitAlternative;
import java.util.List;

public class CliFruit extends CliIngredient<FruitAlternative> {
    public CliFruit() {
        super("fruit", List.of(
                        FruitAlternative.AVOCADO,
                        FruitAlternative.MANGO,
                        FruitAlternative.STRAWBARRIES),
                List.of("Avocado: 2.00 $",
                        "Mango: 1.50 $",
                        "Strawbarries: 1.00 $"));
    }
}
