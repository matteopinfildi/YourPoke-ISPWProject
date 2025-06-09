package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.RiceOption;
import java.util.List;

public class CliRice extends CliIngredient<RiceOption> {
    public CliRice() {
        super("rice", List.of(
                        RiceOption.SUSHI,
                        RiceOption.VENUS,
                        RiceOption.BASMATI),
                List.of("Sushi rice: 3.00 $, 340 cal",
                        "Venus rice: 4.00 $, 360 cal",
                        "Basmati rice: 3.50 $, 350 cal"));
    }
}
