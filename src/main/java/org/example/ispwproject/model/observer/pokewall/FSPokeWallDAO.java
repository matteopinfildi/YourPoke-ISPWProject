package org.example.ispwproject.model.observer.pokewall;

//import org.example.ispwproject.utils.exception.SystemException;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class FSPokeWallDAO implements PokeWallDAO {
//    private static final String FILE_PATH = "posts.csv";
//    private static final String DELIMITER = ",";
//    private static int lastId = 0; // Per mantenere l'ultimo ID usato
//
//    @Override
//    public void create(PokeWall pokeWall) throws SystemException {
//        synchronized (FSPokeWallDAO.class) {
//            // Assicurati che l'ID sia univoco e incrementale
//            if (pokeWall.getId() <= lastId) {
//                lastId++;
//                pokeWall.setId(lastId);
//            } else {
//                lastId = pokeWall.getId();
//            }
//        }
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
//            // Scrivi tutti i campi incluso l'ID
//            writer.write(pokeWall.getId() + DELIMITER +
//                    pokeWall.getUsername() + DELIMITER +
//                    pokeWall.getPokeName() + DELIMITER +
//                    pokeWall.getSize());
//
//            // Aggiungi ingredienti se presenti
//            if (pokeWall.getIngredients() != null && !pokeWall.getIngredients().isEmpty()) {
//                writer.write(DELIMITER + String.join("|", pokeWall.getIngredients()));
//            }
//            writer.newLine();
//        } catch (IOException e) {
//            throw new SystemException("Error saving post to file");
//        }
//    }
//
//    @Override
//    public List<PokeWall> getAllPosts() throws SystemException {
//        List<PokeWall> posts = new ArrayList<>();
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                PokeWall post = parsePostFromLine(line); // Usa il metodo separato
//                if (post != null) {
//                    posts.add(post);
//                }
//            }
//        } catch (IOException e) {
//            throw new SystemException("Error reading posts from file");
//        }
//        return posts;
//    }
//
//    // **Nuovo metodo per estrarre un post da una riga del file**
//    private PokeWall parsePostFromLine(String line) {
//        String[] data = line.split(DELIMITER, -1); // -1 mantiene campi vuoti
//        if (data.length < 4) {
//            return null; // Se la riga non ha abbastanza dati, la ignoriamo
//        }
//
//        try {
//            int id = Integer.parseInt(data[0]);
//            String username = data[1];
//            String pokeName = data[2];
//            String size = data[3];
//
//            // Aggiorna lastId in modo sicuro
//            synchronized (FSPokeWallDAO.class) {
//                if (id > lastId) {
//                    lastId = id;
//                }
//            }
//
//            List<String> ingredients = new ArrayList<>();
//            if (data.length > 4) {
//                ingredients = Arrays.asList(data[4].split("\\|"));
//            }
//
//            PokeWall post = new PokeWall(pokeName, size, username, ingredients);
//            post.setId(id);
//            return post;
//        } catch (NumberFormatException e) {
//            System.err.println("Invalid ID format in line: " + line);
//            return null; // Se c'è un errore nel parsing, ignoriamo la riga
//        }
//    }
//
//
//    @Override
//    public void delete(int postId) throws SystemException {
//        List<String> linesToKeep = new ArrayList<>();
//        boolean found = false;
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (!isPostIdMatch(line, postId)) {
//                    linesToKeep.add(line);
//                } else {
//                    found = true;
//                }
//            }
//        } catch (IOException e) {
//            throw new SystemException("Error reading file for deletion");
//        }
//
//        if (!found) {
//            throw new SystemException("Post with ID " + postId + " not found");
//        }
//
//        // Riscrive il file senza il post eliminato
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
//            for (String line : linesToKeep) {
//                writer.write(line);
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            throw new SystemException("Error rewriting file after deletion");
//        }
//    }
//
//    private boolean isPostIdMatch(String line, int postId) {
//        String[] data = line.split(DELIMITER, -1);
//        if (data.length >= 1) {
//            try {
//                return Integer.parseInt(data[0]) == postId;
//            } catch (NumberFormatException e) {
//                return false;
//            }
//        }
//        return false;
//    }
//
//}

import org.example.ispwproject.utils.exception.SystemException;
import java.io.*;
import java.util.*;

public class FSPokeWallDAO implements PokeWallDAO {
    private static final String POSTS_FILE = "poke_wall_posts.csv";
    private static final String INGREDIENTS_FILE = "poke_wall_ingredients.csv";
    private static final String VIEWS_FILE = "poke_post_views.csv";
    private static final String DELIMITER = "|";

    @Override
    public void create(PokeWall pokeWall) throws SystemException {
        // Genera un nuovo ID univoco
        int postId = getNextPostId();

        try {
            // Salva il post principale
            savePost(postId, pokeWall);

            // Salva gli ingredienti
            saveIngredients(postId, pokeWall.getIngredients());

        } catch (IOException e) {
            throw new SystemException("Error saving post to file: " + e.getMessage());
        }
    }

    private int getNextPostId() throws SystemException {
        List<PokeWall> allPosts = getAllPosts();
        return allPosts.stream().mapToInt(PokeWall::getId).max().orElse(0) + 1;
    }

    private void savePost(int postId, PokeWall pokeWall) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(POSTS_FILE, true))) {
            writer.write(String.join(DELIMITER,
                    String.valueOf(postId),
                    pokeWall.getPokeName(),
                    pokeWall.getSize(),
                    pokeWall.getUsername()));
            writer.newLine();
        }
    }

    private void saveIngredients(int postId, List<String> ingredients) throws IOException {
        if (ingredients == null || ingredients.isEmpty()) return;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INGREDIENTS_FILE, true))) {
            for (String ingredient : ingredients) {
                writer.write(String.join(DELIMITER,
                        String.valueOf(postId),
                        ingredient));
                writer.newLine();
            }
        }
    }

    @Override
    public List<PokeWall> getAllPosts() throws SystemException {
        Map<Integer, PokeWall> postsMap = new LinkedHashMap<>();

        try {
            // Leggi tutti i post
            readPosts(postsMap);

            // Aggiungi gli ingredienti a ciascun post
            readIngredients(postsMap);

        } catch (IOException e) {
            throw new SystemException("Error reading posts: " + e.getMessage());
        }

        return new ArrayList<>(postsMap.values());
    }

    private void readPosts(Map<Integer, PokeWall> postsMap) throws IOException {
        if (!new File(POSTS_FILE).exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(POSTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER);
                if (parts.length >= 4) {
                    int postId = Integer.parseInt(parts[0]);
                    String pokeName = parts[1];
                    String size = parts[2];
                    String username = parts[3];

                    postsMap.put(postId, new PokeWall(pokeName, size, username, new ArrayList<>()));
                }
            }
        }
    }

    private void readIngredients(Map<Integer, PokeWall> postsMap) throws IOException {
        if (!new File(INGREDIENTS_FILE).exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(INGREDIENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER);
                if (parts.length >= 2) {
                    int postId = Integer.parseInt(parts[0]);
                    String ingredient = parts[1];

                    if (postsMap.containsKey(postId)) {
                        postsMap.get(postId).getIngredients().add(ingredient);
                    }
                }
            }
        }
    }

    @Override
    public void delete(int postId) throws SystemException {
        try {
            // Elimina il post principale
            deleteFromFile(POSTS_FILE, postId);

            // Elimina gli ingredienti associati
            deleteFromFile(INGREDIENTS_FILE, postId);

            // Elimina le visualizzazioni associate
            deleteFromFile(VIEWS_FILE, postId);

        } catch (IOException e) {
            throw new SystemException("Error deleting post: " + e.getMessage());
        }
    }

    private void deleteFromFile(String filename, int postId) throws IOException {
        if (!new File(filename).exists()) return;

        List<String> linesToKeep = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER);
                if (parts.length > 0 && !parts[0].equals(String.valueOf(postId))) {
                    linesToKeep.add(line);
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : linesToKeep) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

//    @Override
//    public List<PokeWall> getUnseenPosts(String username) throws SystemException {
//        List<PokeWall> allPosts = getAllPosts();
//        Set<Integer> seenPostIds = getSeenPostIds(username);
//
//        List<PokeWall> unseenPosts = new ArrayList<>();
//
//        for (PokeWall post : allPosts) {
//            if (!seenPostIds.contains(post.getId())) {
//                unseenPosts.add(post);
//            }
//        }
//
//        return unseenPosts;
//    }

    @Override
    public List<PokeWall> getUnseenPosts(String username) throws SystemException {
        List<PokeWall> allPosts = getAllPosts();
        Set<Integer> seenPostIds = getSeenPostIds(username);

        List<PokeWall> unseenPosts = new ArrayList<>();

        // Ordina i post per ID in ordine decrescente (dal più recente al più vecchio)
        allPosts.sort((p1, p2) -> Integer.compare(p2.getId(), p1.getId()));

        for (PokeWall post : allPosts) {
            if (!seenPostIds.contains(post.getId())) {
                unseenPosts.add(post);
                // IMPORTANTE: Non marcare subito come visto qui!
                // Sarà fatto dopo la notifica nel controller
            }
        }

        return unseenPosts;
    }



    private Set<Integer> getSeenPostIds(String username) throws SystemException {
        Set<Integer> seenPostIds = new HashSet<>();

        if (!new File(VIEWS_FILE).exists()) {
            return seenPostIds;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(VIEWS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER);
                if (parts.length >= 2 && parts[1].equals(username)) {
                    seenPostIds.add(Integer.parseInt(parts[0]));
                }
            }
        } catch (IOException e) {
            throw new SystemException("Error reading seen posts: " + e.getMessage());
        }

        return seenPostIds;
    }



//    @Override
//    public void markPostAsSeen(int postId, String username) throws SystemException {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VIEWS_FILE, true))) {
//            writer.write(String.join(DELIMITER,
//                    String.valueOf(postId),
//                    username));
//            writer.newLine();
//        } catch (IOException e) {
//            throw new SystemException("Error marking post as seen: " + e.getMessage());
//        }
//    }

    @Override
    public void markPostAsSeen(int postId, String username) throws SystemException {
        // Verifica se il post è già stato marcato come visto
        if (isPostAlreadySeen(postId, username)) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VIEWS_FILE, true))) {
            writer.write(postId + DELIMITER + username);
            writer.newLine();
        } catch (IOException e) {
            throw new SystemException("Error marking post as seen: " + e.getMessage());
        }
    }

    private boolean isPostAlreadySeen(int postId, String username) throws SystemException {
        if (!new File(VIEWS_FILE).exists()) {
            return false;

        }

        try (BufferedReader reader = new BufferedReader(new FileReader(VIEWS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER, 2);
                if (parts.length == 2 &&
                        parts[0].equals(String.valueOf(postId)) &&
                        parts[1].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new SystemException("Error checking seen status: " + e.getMessage());
        }

        return false;
    }

}