package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.RiceAlternative;
import java.util.List;

public class CliRice extends CliIngredient<RiceAlternative> {
    public CliRice() {
        super("rice", List.of(
                        RiceAlternative.SUSHI,
                        RiceAlternative.VENUS,
                        RiceAlternative.BASMATI),
                List.of("Sushi rice: 3.00 $",
                        "Venus rice: 4.00 $",
                        "Basmati rice: 3.50 $"));
    }
}
