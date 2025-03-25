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
            StringBuilder postLine = new StringBuilder();
            postLine.append(pokeWall.getUsername()).append(DELIMITER)
                    .append(pokeWall.getContent());

            if (pokeWall.getIngredients() != null && !pokeWall.getIngredients().isEmpty()) {
                writer.write(DELIMITER + String.join("|", pokeWall.getIngredients()));  // Separiamo gli ingredienti con "|"
            }

           writer.write("\n");
        } catch (IOException e) {
            throw new SystemException("Error");
        }
    }



    @Override
    public List<PokeWall> getAllPosts() throws SystemException {
        List<PokeWall> posts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(DELIMITER);

                // Verifica se la riga ha almeno 3 colonne (contenuto, username, timestamp)
                if (data.length >= 2) {
                    String content = data[0];  // Nome poke + size
                    String username = data[1];

                    // Leggi gli ingredienti, se ci sono
                    List<String> ingredients = new ArrayList<>();
                    if (data.length > 2) {
                        String[] ingredientArray = data[2].split("\\|");  // Separiamo gli ingredienti con "|"
                        ingredients = Arrays.asList(ingredientArray);
                    }

                    // Crea un nuovo oggetto PokeWall
                    posts.add(new PokeWall(content, username, ingredients));
                } else {
                    System.out.println("Riga malformattata, ignorata: " + line);
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
                    try {
                        int currentId = Integer.parseInt(data[0].trim());
                        if (currentId == postId) {
                            deleted = true;
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Errore nel parsing ID, riga ignorata: " + line);
                    }
                }
                lines.add(line);
            }
        } catch (IOException e) {
            throw new SystemException("Errore durante la lettura del file per la cancellazione");
        }

        if (deleted) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new SystemException("Errore durante la riscrittura del file dopo la cancellazione");
            }
        }
    }

}
