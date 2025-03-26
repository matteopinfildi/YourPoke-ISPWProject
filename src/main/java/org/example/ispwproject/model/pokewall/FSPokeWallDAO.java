package org.example.ispwproject.model.pokewall;

import org.example.ispwproject.utils.exception.SystemException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FSPokeWallDAO implements PokeWallDAO {
    private static final String FILE_PATH = "posts.csv";
    private static final String DELIMITER = ",";

    @Override
    public void create(PokeWall pokeWall) throws SystemException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // Scriviamo i dati nell'ordine: username,pokeName,size,ingredients
            writer.write(pokeWall.getUsername() + DELIMITER +
                    pokeWall.getPokeName() + DELIMITER +
                    pokeWall.getSize());

            // Aggiungi gli ingredienti se presenti
            if (pokeWall.getIngredients() != null && !pokeWall.getIngredients().isEmpty()) {
                writer.write(DELIMITER + String.join("|", pokeWall.getIngredients()));
            }
            writer.newLine();
        } catch (IOException e) {
            throw new SystemException("Error saving post to file");
        }
    }

    @Override
    public List<PokeWall> getAllPosts() throws SystemException {
        List<PokeWall> posts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(DELIMITER, -1); // -1 per mantenere campi vuoti

                if (data.length >= 3) {
                    String username = data[0];
                    String pokeName = data[1];
                    String size = data[2];

                    List<String> ingredients = new ArrayList<>();
                    if (data.length > 3) {
                        ingredients = Arrays.asList(data[3].split("\\|"));
                    }

                    posts.add(new PokeWall(pokeName, size, username, ingredients));
                }
            }
        } catch (IOException e) {
            throw new SystemException("Error reading posts from file");
        }
        return posts;
    }



    @Override
    public void delete(int postId) throws SystemException {
        List<String> lines = new ArrayList<>();
        boolean deleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(DELIMITER, -1);
                if (data.length >= 3) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new SystemException("Errore durante la lettura del file per la cancellazione");
        }

        if (postId >= 0 && postId < lines.size()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (int i = 0; i < lines.size(); i++) {
                    if (i != postId) {  // Saltiamo la riga da eliminare
                        writer.write(lines.get(i));
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                throw new SystemException("Errore durante la riscrittura del file dopo la cancellazione");
            }
        }
    }

}
