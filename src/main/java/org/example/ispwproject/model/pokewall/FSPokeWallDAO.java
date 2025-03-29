package org.example.ispwproject.model.pokewall;

import org.example.ispwproject.utils.exception.SystemException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FSPokeWallDAO implements PokeWallDAO {
    private static final String FILE_PATH = "posts.csv";
    private static final String DELIMITER = ",";
    private static int lastId = 0; // Per mantenere l'ultimo ID usato

    @Override
    public void create(PokeWall pokeWall) throws SystemException {
        synchronized (FSPokeWallDAO.class) {
            // Assicurati che l'ID sia univoco e incrementale
            if (pokeWall.getId() <= lastId) {
                lastId++;
                pokeWall.setId(lastId);
            } else {
                lastId = pokeWall.getId();
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // Scrivi tutti i campi incluso l'ID
            writer.write(pokeWall.getId() + DELIMITER +
                    pokeWall.getUsername() + DELIMITER +
                    pokeWall.getPokeName() + DELIMITER +
                    pokeWall.getSize());

            // Aggiungi ingredienti se presenti
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
                PokeWall post = parsePostFromLine(line); // Usa il metodo separato
                if (post != null) {
                    posts.add(post);
                }
            }
        } catch (IOException e) {
            throw new SystemException("Error reading posts from file");
        }
        return posts;
    }

    // **Nuovo metodo per estrarre un post da una riga del file**
    private PokeWall parsePostFromLine(String line) {
        String[] data = line.split(DELIMITER, -1); // -1 mantiene campi vuoti
        if (data.length < 4) {
            return null; // Se la riga non ha abbastanza dati, la ignoriamo
        }

        try {
            int id = Integer.parseInt(data[0]);
            String username = data[1];
            String pokeName = data[2];
            String size = data[3];

            // Aggiorna lastId in modo sicuro
            synchronized (FSPokeWallDAO.class) {
                if (id > lastId) {
                    lastId = id;
                }
            }

            List<String> ingredients = new ArrayList<>();
            if (data.length > 4) {
                ingredients = Arrays.asList(data[4].split("\\|"));
            }

            PokeWall post = new PokeWall(pokeName, size, username, ingredients);
            post.setId(id);
            return post;
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format in line: " + line);
            return null; // Se c'è un errore nel parsing, ignoriamo la riga
        }
    }


    @Override
    public void delete(int postId) throws SystemException {
        List<String> linesToKeep = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(DELIMITER, -1);
                if (data.length >= 1) {
                    try {
                        int currentId = Integer.parseInt(data[0]);
                        if (currentId != postId) {
                            linesToKeep.add(line);
                        } else {
                            found = true;
                        }
                    } catch (NumberFormatException e) {
                        // Salta righe malformate
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            throw new SystemException("Error reading file for deletion");
        }

        if (!found) {
            throw new SystemException("Post with ID " + postId + " not found");
        }

        // Risalva il file senza il post eliminato
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : linesToKeep) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new SystemException("Error rewriting file after deletion");
        }
    }
}