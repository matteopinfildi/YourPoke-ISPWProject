package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.ProteinOption;
import java.util.List;

public class CliProtein extends CliIngredient<ProteinOption> {
    public CliProtein() {
        super("protein", List.of(
                        ProteinOption.SALMON,
                        ProteinOption.SHRIMP,
                        ProteinOption.TUNA),
                List.of("Salmon: 4.00 $, 208 cal",
                        "Shrimp: 3.00 $, 85 cal",
                        "Tuna: 3.50 $, 144 cal"));
    }
}