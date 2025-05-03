package org.example.ispwproject.model.decorator.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.*;
import org.example.ispwproject.utils.exception.SystemException;

import java.io.*;
import java.util.*;

public class FSPokeLabDAO implements PokeLabDAO {
    private static final String FILE_LAB = "poke_lab.csv";
    private static final String FILE_INGREDIENTS = "poke_lab_ingredients.csv";
    private static final String DELIMITER = ",";
    private static int lastId = 0;

    @Override
    public void create(PokeLab pokeLab) throws SystemException {
        synchronized (FSPokeLabDAO.class) {
            if (pokeLab.id() <= lastId) {
                lastId++;
                pokeLab.setId(lastId);
            } else {
                lastId = pokeLab.id();
            }
        }

        try (BufferedWriter writerLab = new BufferedWriter(new FileWriter(FILE_LAB, true));
             BufferedWriter writerIng = new BufferedWriter(new FileWriter(FILE_INGREDIENTS, true))) {

            // Salva PokeLab
            writerLab.write(pokeLab.id() + DELIMITER + pokeLab.price() + DELIMITER + pokeLab.getBowlSize());
            writerLab.newLine();

            // Salva ingredienti
            for (Map.Entry<String, GenericAlternative> entry : pokeLab.allIngredients().entrySet()) {
                writerIng.write(pokeLab.id() + DELIMITER + entry.getKey() + DELIMITER + ((Enum<?>) entry.getValue()).name());
                writerIng.newLine();
            }

        } catch (IOException e) {
            throw new SystemException("Error saving PokeLab to file");
        }
    }

    @Override
    public PokeLab read(int plid) throws SystemException {
        double price = 0;
        String size = null;
        Map<String, GenericAlternative> ingredients = new HashMap<>();

        boolean found = false;

        try (BufferedReader readerLab = new BufferedReader(new FileReader(FILE_LAB))) {
            String line;
            while ((line = readerLab.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                if (Integer.parseInt(parts[0]) == plid) {
                    price = Double.parseDouble(parts[1]);
                    size = parts[2];
                    found = true;
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new SystemException("Error reading PokeLab file");
        }

        if (!found) {
            return null;
        }

        try (BufferedReader readerIng = new BufferedReader(new FileReader(FILE_INGREDIENTS))) {
            String line;
            while ((line = readerIng.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                if (Integer.parseInt(parts[0]) == plid) {
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
        } catch (IOException | NumberFormatException e) {
            throw new SystemException("Error reading PokeLab ingredients file");
        }

        return new PokeLab(price, plid, ingredients, size);
    }

    @Override
    public void delete(int plid) throws SystemException {
        try {
            // Rimuovi da poke_lab.csv
            File originalLab = new File(FILE_LAB);
            File tempLab = new File("temp_lab.csv");

            try (BufferedReader reader = new BufferedReader(new FileReader(originalLab));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempLab))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith(plid + DELIMITER)) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }

            if (!originalLab.delete() || !tempLab.renameTo(originalLab)) {
                throw new IOException("Error updating poke_lab file");
            }

            // Rimuovi da poke_lab_ingredients.csv
            File originalIng = new File(FILE_INGREDIENTS);
            File tempIng = new File("temp_ingredients.csv");

            try (BufferedReader reader = new BufferedReader(new FileReader(originalIng));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempIng))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith(plid + DELIMITER)) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }

            if (!originalIng.delete() || !tempIng.renameTo(originalIng)) {
                throw new IOException("Error updating poke_lab_ingredients file");
            }

        } catch (IOException e) {
            throw new SystemException("Error deleting PokeLab from file: " + e.getMessage());
        }
    }

    @Override
    public void updateBowlSize(int plid, String bowlSize) throws SystemException {
        File original = new File(FILE_LAB);
        File temp = new File("temp_lab.csv");
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(original));
             BufferedWriter writer = new BufferedWriter(new FileWriter(temp))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                if (Integer.parseInt(parts[0]) == plid) {
                    writer.write(parts[0] + DELIMITER + parts[1] + DELIMITER + bowlSize);
                    updated = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

        } catch (IOException | NumberFormatException e) {
            throw new SystemException("Error updating PokeLab bowl size");
        }

        if (!original.delete() || !temp.renameTo(original)) {
            throw new SystemException("Could not replace original file during bowl size update");
        }

        if (!updated) {
            System.out.println("Nessun record trovato con ID: " + plid);
        } else {
            System.out.println("Bowl size aggiornato per ID: " + plid);
        }
    }
}

