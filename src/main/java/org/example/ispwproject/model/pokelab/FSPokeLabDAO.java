package org.example.ispwproject.model.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.*;
import org.example.ispwproject.utils.exception.SystemException;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class FSPokeLabDAO implements PokeLabDAO {

    private static final String FILE_LAB = "poke_lab.csv";
    private static final String FILE_INGREDIENTS = "poke_lab_ingredients.csv";
    private static final String TEMP_LAB =  "temp_lab.csv"; // file temporanei usati per l'aggiornamento e la cancellazione
    private static final String TEMP_ING = "temp_ingredients.csv"; // file temporanei usati per l'aggiornamento e la cancellazione
    private static final String DELIMITER = ",";
    private static int lastId = -1; // inizializzato a -1, ma verrà aggiornato leggendo il file
    private static final Logger LOGGER = Logger.getLogger(FSPokeLabDAO.class.getName());


    // carica l'ultimo id all'avvio, per cui chiama l'id maggiore
    static {
        try {
            lastId = findMaxIdFromFile();
        } catch (SystemException e) {
            LOGGER.severe("Error initializing lastId: " + e.getMessage());
            lastId = 0; // Fallback
        }
    }

    // metodo che trova l'id maggiore presente nel file
    private static int findMaxIdFromFile() throws SystemException {
        if (!new File(FILE_LAB).exists()) {
            return 0;
        }
        int maxId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_LAB))) {
            String line;
            while ((line = reader.readLine()) != null) {
                maxId = updateMaxIdIfValid(line, maxId); // viene richiamato per ciascuna riga ed estrae l'id di ogni poke
            }
        } catch (IOException _) {
            throw new SystemException("Error reading PokeLab file to find max ID");
        }

        return maxId;
    }

    // metodo che serve ad aggiornare l'id piu grande presente nel file
    private static int updateMaxIdIfValid(String line, int currentMaxId) {
        String[] parts = line.split(DELIMITER);
        if (parts.length > 0) {
            try {
                int currentId = Integer.parseInt(parts[0]);
                if (currentId > currentMaxId) {
                    // se il nuovo id è maggiore rispetto all'id massimo attuale, allora viene ritornato
                    return currentId;
                }
            } catch (NumberFormatException _) {
                LOGGER.warning("ID non numerico trovato nella riga: " + line);
            }
        }
        // altrimenti si returna il vecchio id massimo
        return currentMaxId;
    }


    @Override
    public void create(PokeLab pokeLab) throws SystemException {
        synchronized (FSPokeLabDAO.class) {
            // assegna un nuovo id solo se non è già stato assegnato
            if (pokeLab.id() <= 0) {
                // se entra qua vuol dire che il poke lab non ha ancora un id valito assegnato
                lastId++;
                pokeLab.setId(lastId);
            } else if (pokeLab.id() > lastId) {
                // aggiorna lastId se viene passato un ID maggiore
                lastId = pokeLab.id();
            }
        }
        // apriamo i file in modalità append, così da scrivere in coda
        try (BufferedWriter writerLab = new BufferedWriter(new FileWriter(FILE_LAB, true));
             BufferedWriter writerIng = new BufferedWriter(new FileWriter(FILE_INGREDIENTS, true))) {
            // salva PokeLab, inserendo tutte le caratteristiche associate
            writerLab.write(pokeLab.id() + DELIMITER + pokeLab.price() + DELIMITER + pokeLab.getBowlSize() + DELIMITER + pokeLab.calories() );
            writerLab.newLine(); // va a capo
            // salva ingredienti relativi al poke lab
            for (Map.Entry<String, GenericAlternative> entry : pokeLab.allIngredients().entrySet()) {
                writerIng.write(pokeLab.id() + DELIMITER + entry.getKey() + DELIMITER + ((Enum<?>) entry.getValue()).name());
                writerIng.newLine();
            }
        } catch (IOException _) {
            throw new SystemException("Error saving PokeLab to file");
        }
    }

    @Override
    public PokeLab read(int plid) throws SystemException {
        // sono delle variabili che mantengono temporaneamente i valori letti dal file poke_lab
        double price = 0;
        String size = null;
        int calories = 0;
        Map<String, GenericAlternative> ingredients = new HashMap<>();
        boolean found = false;
        try (BufferedReader readerLab = new BufferedReader(new FileReader(FILE_LAB))) {
            String line;
            // scorriamo su tutte le righe del file
            while ((line = readerLab.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                // viene convertito il primo campo (quello del plid) e viene confrontato con quello passato al metodo
                if (Integer.parseInt(parts[0]) == plid) {
                    // se corrisponde si salvano il prezzo, la size e le calorie e si pone il flag found = true
                    price = Double.parseDouble(parts[1]);
                    size = parts[2];
                    calories = Integer.parseInt(parts[3]);
                    found = true;
                    break;
                }
            }
        } catch (IOException | NumberFormatException _) {
            throw new SystemException("Error reading PokeLab file");
        }
        if (!found) {
            return null;
        }
        // si fa la stessa cosa fatta per il file poke_lab.csv, anche per il file degli ingredienti
        try (BufferedReader readerIng = new BufferedReader(new FileReader(FILE_INGREDIENTS))) {
            String line;
            // si scorrono tutte le righe del gile
            while ((line = readerIng.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                // viene convertito il primo campo (quello del plid) e viene confrontato con quello passato al metodo
                if (Integer.parseInt(parts[0]) == plid) {
                    // se corrispondono si salvano gli ingredienti con le relative alternative
                    String name = parts[1];
                    String alternative = parts[2];
                    GenericAlternative value = switch (name) {
                        case "rice" -> Enum.valueOf(RiceAlternative.class, alternative);
                        case "protein" -> Enum.valueOf(ProteinAlternative.class, alternative);
                        case "fruit" -> Enum.valueOf(FruitAlternative.class, alternative);
                        case "crunchy" -> Enum.valueOf(CrunchyAlternative.class, alternative);
                        case "sauces" -> Enum.valueOf(SaucesAlternative.class, alternative);
                        default -> null;
                    };
                    if (value != null) {
                        ingredients.put(name, value);
                    }
                }
            }
        } catch (IOException | NumberFormatException _) {
            throw new SystemException("Error reading PokeLab ingredients file");
        }
        return new PokeLab(price, plid, ingredients, size, calories);
    }

    @Override
    public void delete(int plid) throws SystemException {
        try {
            // rimuovi da poke_lab.csv
            File originalLab = new File(FILE_LAB);
            File tempLab = new File(TEMP_LAB);
            try (BufferedReader reader = new BufferedReader(new FileReader(originalLab));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempLab))) {
                String line;
                // si scorrono tutte le righe del file poke_lab.csv
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith(plid + DELIMITER)) {
                        //  se una riga non inizia con plid+DELIMITERviene copiata nel file temporaneo
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
            // con la prima condizione si cancella il file originale e si rinomina quello temporaneo, come file originale
            if (!originalLab.delete() || !tempLab.renameTo(originalLab)) {
                throw new IOException("Error updating poke_lab file");
            }
            // rimuovi da poke_lab_ingredients.csv
            File originalIng = new File(FILE_INGREDIENTS);
            File tempIng = new File(TEMP_ING);
            try (BufferedReader reader = new BufferedReader(new FileReader(originalIng));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempIng))) {
                String line;
                // si scorrono tutte le righe del file poke_lab_ingredients.csv
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith(plid + DELIMITER)) {
                        //  se una riga non inizia con plid+DELIMITERviene copiata nel file temporaneo
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
            // con la prima condizione si cancella il file originale e si rinomina quello temporaneo, come file originale
            if (!originalIng.delete() || !tempIng.renameTo(originalIng)) {
                throw new IOException("Error updating poke_lab_ingredients file");
            }
        } catch (IOException e) {
            throw new SystemException("Error deleting PokeLab from file: " + e.getMessage());
        }
    }


    @Override
    public void update(PokeLab pokeLab) throws SystemException {
        // 1) aggiorna il record in poke_lab.csv (price e size)
        File origLab = new File(FILE_LAB);
        File tmpLab = new File(TEMP_LAB);
        try (BufferedReader r = new BufferedReader(new FileReader(origLab));
             BufferedWriter w = new BufferedWriter(new FileWriter(tmpLab))) {
            String line;
            // si scorrono tutte le righe
            while ((line = r.readLine()) != null) {
                String[] p = line.split(DELIMITER);
                if (Integer.parseInt(p[0]) == pokeLab.id()) {
                    // controlla se gli id corrispondono, se è cosi, scrive nel file temporaneo una nuova riga con i campi aggiornati
                    w.write(pokeLab.id() + DELIMITER + pokeLab.price() + DELIMITER + pokeLab.getBowlSize() + DELIMITER + pokeLab.calories());
                } else {
                    // altrimenti copia la riga così come è
                    w.write(line);
                }
                w.newLine(); // va a capo
            }
        } catch (IOException|NumberFormatException e) {
            throw new SystemException("Error updating PokeLab file: " + e.getMessage());
        }
        if (!origLab.delete() || !tmpLab.renameTo(origLab)) {
            throw new SystemException("Could not replace poke_lab.csv during update");
        }

        // 2) aggiorna poke_lab_ingredients.csv: elimina e reinserisci
        File origIng = new File(FILE_INGREDIENTS);
        File tmpIng = new File(TEMP_ING);
        try (BufferedReader r = new BufferedReader(new FileReader(origIng));
             BufferedWriter w = new BufferedWriter(new FileWriter(tmpIng))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (!line.startsWith(pokeLab.id() + DELIMITER)) {
                    w.write(line);
                    w.newLine();
                }
            }
            // ora append dei nuovi ingredienti
            for (var e : pokeLab.allIngredients().entrySet()) {
                w.write(pokeLab.id() + DELIMITER + e.getKey() + DELIMITER + ((Enum<?>) e.getValue()).name());
                w.newLine();
            }
        } catch (IOException e) {
            throw new SystemException("Error updating ingredients file: " + e.getMessage());
        }
        // con la prima condizione si cancella il file originale e si rinomina quello temporaneo, come file originale
        if (!origIng.delete() || !tmpIng.renameTo(origIng)) {
            throw new SystemException("Could not replace poke_lab_ingredients.csv during update");
        }
    }

}
