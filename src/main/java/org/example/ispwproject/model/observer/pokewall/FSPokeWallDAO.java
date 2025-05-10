package org.example.ispwproject.model.observer.pokewall;

import org.example.ispwproject.utils.exception.SystemException;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FSPokeWallDAO implements PokeWallDAO {
    private static final String POSTS_FILE = "poke_wall_posts.csv";
    private static final String INGREDIENTS_FILE = "poke_wall_ingredients.csv";
    private static final String VIEWS_FILE = "poke_post_views.csv";
    private static final String DELIMITER = "|";

    @Override
    public void create(PokeWall pokeWall) throws SystemException {
        int postId = getNextPostId();

        try {
            savePost(postId, pokeWall);
            saveIngredients(postId, pokeWall.getIngredients());
        } catch (IOException e) {
            throw new SystemException("Error saving post to file: " + e.getMessage());
        }
    }

    private int getNextPostId() throws SystemException {
        List<PokeWall> allPosts = getAllPosts();
        int maxId = allPosts.stream()
                .mapToInt(PokeWall::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }

    private void savePost(int postId, PokeWall pokeWall) throws IOException {
        File file = new File(POSTS_FILE);
        boolean fileExists = file.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (!fileExists) {
                writer.write("id|poke_name|size|username");
                writer.newLine();
            }
            String line = String.join(DELIMITER,
                    String.valueOf(postId),
                    pokeWall.getPokeName(),
                    pokeWall.getSize(),
                    pokeWall.getUsername());
            writer.write(line);
            writer.newLine();
        }
    }

    private void saveIngredients(int postId, List<String> ingredients) throws IOException {
        if (ingredients == null || ingredients.isEmpty()) {
            return;
        }

        File file = new File(INGREDIENTS_FILE);
        boolean fileExists = file.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (!fileExists) {
                writer.write("post_id|ingredient");
                writer.newLine();
            }
            for (String ingredient : ingredients) {
                String line = String.join(DELIMITER,
                        String.valueOf(postId),
                        ingredient);
                writer.write(line);
                writer.newLine();
            }
        }
    }

    @Override
    public List<PokeWall> getAllPosts() throws SystemException {
        Map<Integer, PokeWall> postsMap = new LinkedHashMap<>();

        try {
            readPosts(postsMap);
            readIngredients(postsMap);
        } catch (IOException e) {
            throw new SystemException("Error reading posts: " + e.getMessage());
        }

        return new ArrayList<>(postsMap.values());
    }

    private void readPosts(Map<Integer, PokeWall> postsMap) throws IOException {
        File file = new File(POSTS_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine(); // Skip header, but capture the value
            if (header == null) {
                return; // File is empty or only contains header line
            }


            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER, -1);
                if (parts.length >= 4) {
                    try {
                        int postId = Integer.parseInt(parts[0]);
                        String pokeName = parts[1];
                        String size = parts[2];
                        String username = parts[3];

                        postsMap.put(postId, new PokeWall(postId, pokeName, size, username, new ArrayList<>()));
                    } catch (NumberFormatException _) {
                        // Ignora riga con formato errato
                    }
                }
            }
        }
    }

    private void readIngredients(Map<Integer, PokeWall> postsMap) throws IOException {
        File file = new File(INGREDIENTS_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine(); // Skip header, but capture the value
            if (header == null) {
                return; // File is empty or only contains header line
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER, -1);
                if (parts.length >= 2) {
                    try {
                        int postId = Integer.parseInt(parts[0]);
                        String ingredient = parts[1];

                        if (postsMap.containsKey(postId)) {
                            postsMap.get(postId).getIngredients().add(ingredient);
                        }
                    } catch (NumberFormatException _) {
                        // Ignora riga con formato errato
                    }
                }
            }
        }
    }

    @Override
    public List<PokeWall> getUnseenPosts(String username) throws SystemException {
        List<PokeWall> allPosts = getAllPosts();
        if (allPosts.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Integer> seenPostIds = getSeenPostIds(username);

        List<PokeWall> unseenPosts = allPosts.stream()
                .filter(post -> !seenPostIds.contains(post.getId()))
                .sorted(Comparator.comparingInt(PokeWall::getId).reversed())
                .toList();

        return unseenPosts;
    }

    private Set<Integer> getSeenPostIds(String username) throws SystemException {
        Set<Integer> seenPostIds = new HashSet<>();

        File file = new File(VIEWS_FILE);
        if (!file.exists()) {
            return seenPostIds;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine(); // Skip header
            if (header == null) {
                return seenPostIds; // File vuoto
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER, -1);
                if (parts.length >= 2 && parts[1].equals(username)) {
                    tryAddSeenPostId(seenPostIds, parts[0]);

                }
            }
        } catch (IOException e) {
            throw new SystemException("Error reading seen posts: " + e.getMessage());
        }

        return seenPostIds;
    }

    private void tryAddSeenPostId(Set<Integer> seenPostIds, String idStr) {
        try {
            seenPostIds.add(Integer.parseInt(idStr));
        } catch (NumberFormatException _) {
            // Ignora riga con formato errato
        }
    }


    @Override
    public void markPostAsSeen(int postId, String username) throws SystemException {
        if (postId <= 0) {
            throw new IllegalArgumentException("ID post non valido: " + postId);
        }

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username non valido");
        }

        if (isPostAlreadySeen(postId, username)) {
            return;
        }

        File file = new File(VIEWS_FILE);
        boolean fileExists = file.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (!fileExists) {
                writer.write("post_id|username");
                writer.newLine();
            }
            writer.write(postId + DELIMITER + username);
            writer.newLine();
        } catch (IOException e) {
            throw new SystemException("Error marking post as seen: " + e.getMessage());
        }
    }

    private boolean isPostAlreadySeen(int postId, String username) throws SystemException {
        File file = new File(VIEWS_FILE);
        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine(); // Skip header, but capture the value

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER, -1);
                if (parts.length >= 2 &&
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

    @Override
    public void delete(int postId) throws SystemException {
        try {
            deleteFromFile(POSTS_FILE, postId);
            deleteFromFile(INGREDIENTS_FILE, postId);
            deleteFromFile(VIEWS_FILE, postId);
        } catch (IOException e) {
            throw new SystemException("Error deleting post: " + e.getMessage());
        }
    }

    private void deleteFromFile(String filename, int postId) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }

        List<String> linesToKeep = new ArrayList<>();
        boolean headerSkipped = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!headerSkipped && (line.contains("id|") || line.contains("post_id|") || line.contains("username|"))) {
                    linesToKeep.add(line);
                    headerSkipped = true;
                    continue;
                }

                if (!line.startsWith(postId + DELIMITER)) {
                    linesToKeep.add(line);
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : linesToKeep) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
