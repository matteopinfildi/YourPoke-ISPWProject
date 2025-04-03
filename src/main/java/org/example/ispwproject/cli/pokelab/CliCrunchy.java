package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.CrunchyAlternative;
import java.util.List;

public class CliCrunchy extends CliIngredient<CrunchyAlternative> {
    public CliCrunchy() {
        super("crunchy", List.of(
                        CrunchyAlternative.ONION,
                        CrunchyAlternative.NUTS,
                        CrunchyAlternative.ALMONDS),
                List.of("Onion: 2.00 $",
                        "Nuts: 1.00 $",
                        "Almonds: 1.50 $"));
    }
}
