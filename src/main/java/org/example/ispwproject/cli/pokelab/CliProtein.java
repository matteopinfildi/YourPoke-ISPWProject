package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.ProteinAlternative;
import java.util.List;

public class CliProtein extends CliIngredient<ProteinAlternative> {
    public CliProtein() {
        super("protein", List.of(
                        ProteinAlternative.SALMON,
                        ProteinAlternative.SHRIMP,
                        ProteinAlternative.TUNA),
                List.of("Salmon: 4.00 $, 208 cal",
                        "Shrimp: 3.00 $, 85 cal",
                        "Tuna: 3.50 $, 144 cal"));
    }
}