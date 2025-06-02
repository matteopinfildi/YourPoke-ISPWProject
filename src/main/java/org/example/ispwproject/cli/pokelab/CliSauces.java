package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.SaucesAlternative;
import java.util.List;

public class CliSauces extends CliIngredient<SaucesAlternative> {
    public CliSauces() {
        super("sauces", List.of(
                        SaucesAlternative.TERIYAKI,
                        SaucesAlternative.SOY,
                        SaucesAlternative.WASABI),
                List.of("Teriyaki: 2.00 $, 450 cal",
                        "Soy: 1.00 $, 600 cal",
                        "Wasabi: 1.50 $, 580 cal"));
    }
}
