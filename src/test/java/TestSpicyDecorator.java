import org.example.ispwproject.model.decorator.spicy.SpicyDecorator;
import org.example.ispwproject.model.decorator.pokelab.PokeLab;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSpicyDecorator {

    private PokeLab pokeLab;
    private SpicyDecorator spicyDecorator;

    @BeforeEach
    public void setUp() {
        // Crea un oggetto PokeLab con un prezzo base di 5.0 (ad esempio un poke bowl base)
        pokeLab = new PokeLab(5.0);
    }

    @Test
    public void testSpicyDecoratorPrice() {
        // Aggiungi la spezia "Hebanero" al PokeLab tramite SpicyDecorator
        spicyDecorator = new SpicyDecorator(pokeLab, "Hebanero");

        // Calcola il prezzo finale del poke bowl con la spezia
        double expectedPrice = 5.0 + 0.5; // prezzo base + extra per la spezia (0.5)
        double actualPrice = spicyDecorator.price();

        // Verifica che il prezzo finale sia quello previsto
        assertEquals(expectedPrice, actualPrice, "Il prezzo finale con la spezia hebanero dovrebbe essere corretto");
    }

    @Test
    public void testSpicyDecoratorPriceWithDifferentSpicy() {
        // Aggiungi la spezia "Chili" al PokeLab tramite SpicyDecorator
        spicyDecorator = new SpicyDecorator(pokeLab, "Chili");

        // Calcola il prezzo finale del poke bowl con la spezia
        double expectedPrice = 5.0 + 0.5; // prezzo base + extra per la spezia (0.5)
        double actualPrice = spicyDecorator.price();

        // Verifica che il prezzo finale sia quello previsto anche con spezia "chili"
        assertEquals(expectedPrice, actualPrice, "Il prezzo finale con la spezia chili dovrebbe essere corretto");
    }
}
