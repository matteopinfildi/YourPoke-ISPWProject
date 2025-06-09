package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.SaucesOption;
import java.util.List;

public class CliSauces extends CliIngredient<SaucesOption> {
    public CliSauces() {
        super("sauces", List.of(
                        SaucesOption.TERIYAKI,
                        SaucesOption.SOY,
                        SaucesOption.WASABI),
                List.of("Teriyaki: 2.00 $, 450 cal",
                        "Soy: 1.00 $, 600 cal",
                        "Wasabi: 1.50 $, 580 cal"));
    }
}
