package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.CrunchyOption;
import java.util.List;

public class CliCrunchy extends CliIngredient<CrunchyOption> {
    public CliCrunchy() {
        super("crunchy", List.of(
                        CrunchyOption.ONION,
                        CrunchyOption.NUTS,
                        CrunchyOption.ALMONDS),
                List.of("Onion: 2.00 $, 450 cal",
                        "Nuts: 1.00 $, 600 cal",
                        "Almonds: 1.50 $, 580 cal"));
    }
}
