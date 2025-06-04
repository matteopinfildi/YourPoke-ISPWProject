package org.example.ispwproject.model.observer.pokewall;

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
        int postId = getNextPostId();

        try {
            // vengono salvate id, nome, username e size nel file dei post
            savePost(postId, pokeWall);
            // vengono salvati gli ingredienti e l'id associato nel file degli ingredienti
            saveIngredients(postId, pokeWall.getIngredients());
        } catch (IOException e) {
            throw new SystemException("Error saving post to file: " + e.getMessage());
        }
    }

    // metodo che legge tutti i post salvati e restituisce il massimo id già esistente + 1
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

        // crea il file o lo apre in append, cosi da poter scrivere direttamente alla fine
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (!fileExists) {
                // se è la prima volta che scriviamo in questo file, scrive questo
                writer.write("id|poke_name|size|username");
                writer.newLine(); // va a capo
            }
            // scrive tutte le info relative ad un post, ma non gli ingredienti
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

        // crea il file o lo apre in append, cosi da poter scrivere direttamente alla fine
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (!fileExists) {
                // se è la prima volta che scriviamo in questo file, scrive questo
                writer.write("post_id|ingredient");
                writer.newLine();
            }
            // scrive tutti gli ingredienti relativi ad un post, associati all'id del post
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
        // la LinkedHashMap mantiene l'ordine di inserimento
        Map<Integer, PokeWall> postsMap = new LinkedHashMap<>();

        try {
            // salva tutti i post su postsMap
            readPosts(postsMap);
            readIngredients(postsMap);
        } catch (IOException e) {
            throw new SystemException("Error reading posts: " + e.getMessage());
        }

        // converte i valori dei post presenti su postsMap in una ArrayList
        return new ArrayList<>(postsMap.values());
    }

    private void readPosts(Map<Integer, PokeWall> postsMap) throws IOException {
        File file = new File(POSTS_FILE);
        if (!file.exists()) {
            // se il file non esiste non c'è nulla da leggere
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // scartiamo la riga di intestazione
            String header = reader.readLine();
            if (header == null) {
                return;
            }


            String line;
            // scorriamo su tutto il file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER, -1);
                if (parts.length >= 4) {
                    // se la riga contiene almeno 4 campi va letta, altrimenti si ignora
                    try {
                        // si associano i valori letti sul file a delle variabili
                        int postId = Integer.parseInt(parts[0]); // conversione del primo campo del file in intero, ovvero l'id del post
                        String pokeName = parts[1];
                        String size = parts[2];
                        String username = parts[3];

                        postsMap.put(postId, new PokeWall(postId, pokeName, size, username, new ArrayList<>()));
                    } catch (NumberFormatException _) {
                        // ignora riga con formato errato
                    }
                }
            }
        }
    }

    private void readIngredients(Map<Integer, PokeWall> postsMap) throws IOException {
        File file = new File(INGREDIENTS_FILE);
        if (!file.exists()) {
            // se il file non esiste non c'è nulla da leggere
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // scartiamo la riga di intestazione
            String header = reader.readLine();
            if (header == null) {
                return;
            }

            String line;
            // scorriamo su tutto il file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER, -1);
                if (parts.length >= 2) {
                    // se la riga contiene almeno 2 campi va letta, altrimenti si ignora
                    try {
                        // si associano i valori letti sul file a delle variabili
                        int postId = Integer.parseInt(parts[0]); // conversione del primo campo del file in intero, ovvero l'id del post
                        String ingredient = parts[1];

                        if (postsMap.containsKey(postId)) {
                            postsMap.get(postId).getIngredients().add(ingredient);
                        }
                    } catch (NumberFormatException _) {
                        // ignora riga con formato errato
                    }
                }
            }
        }
    }


    @Override
    public List<PokeWall> getUnseenPosts(String username) throws SystemException {
        List<PokeWall> allPosts = getAllPosts(); // lista completa dei post presenti
        if (allPosts.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Integer> seenPostIds = getSeenPostIds(username); // tutti i post che lo user ha già visto
        // restituisce tutti i post che un utente non ha ancora visualizzato
        return allPosts.stream()
                .filter(post -> !seenPostIds.contains(post.getId()))
                .sorted(Comparator.comparingInt(PokeWall::getId).reversed())
                .toList();
    }


    private Set<Integer> getSeenPostIds(String username) throws SystemException {
        Set<Integer> seenPostIds = new HashSet<>(); // ci sono tutti gli id dei post che l'utente ha già visto

        File file = new File(VIEWS_FILE);
        if (!file.exists()) {
            return seenPostIds;
        }

        // si apre il file in lettura
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            if (header == null) {
                return seenPostIds;
            }

            String line;
            // si leggono tutte le righe del file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER, -1);
                // controlla se ci sono almeno 2 campi e se lo username combacia
                if (parts.length >= 2 && parts[1].equals(username)) {
                    tryAddSeenPostId(seenPostIds, parts[0]); // viene richiamato per convertire il primo campo in intero (ovvero quello dell'id)

                }
            }
        } catch (IOException e) {
            throw new SystemException("Error reading seen posts: " + e.getMessage());
        }

        // vengono restituiti i post già visti dallo user
        return seenPostIds;
    }

    // metodo usato per convertire il primo campo (ovvero quello dell'id)
    private void tryAddSeenPostId(Set<Integer> seenPostIds, String idStr) {
        try {
            seenPostIds.add(Integer.parseInt(idStr));
        } catch (NumberFormatException _) {
            // ignora riga con formato errato
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

        // viene verificato se il post è già stato visto
        if (isPostAlreadySeen(postId, username)) {
            return;
        }

        File file = new File(VIEWS_FILE);
        boolean fileExists = file.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (!fileExists) {
                // se il file non è mai stato usato scrive:
                writer.write("post_id|username");
                writer.newLine();
            }
            // scrive l'id del post visualizzato e lo username che lo ha visualizzato
            writer.write(postId + DELIMITER + username);
            writer.newLine();
        } catch (IOException e) {
            throw new SystemException("Error marking post as seen: " + e.getMessage());
        }
    }

    // metodo che controlla se un post è già stato visualizzato
    private boolean isPostAlreadySeen(int postId, String username) throws SystemException {
        File file = new File(VIEWS_FILE);
        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            // si scorrono tutte le righe del file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\" + DELIMITER, -1);
                if (parts.length >= 2 &&
                        parts[0].equals(String.valueOf(postId)) &&
                        parts[1].equals(username)) {
                    // se la riga ha almeno 2 colonne e id del post e username combaciano, allora il post è stato già visto
                    return true;
                }
            }
        } catch (IOException e) {
            throw new SystemException("Error checking seen status: " + e.getMessage());
        }

        // altrimenti il post ancora non è stato visto
        return false;
    }

    @Override
    public void delete(int postId) throws SystemException {
        try {
            // vengono rimossi i post e tutti i suoi riferimenti che hanno l'id passato al metodo
            deleteFromFile(POSTS_FILE, postId);
            deleteFromFile(INGREDIENTS_FILE, postId);
            deleteFromFile(VIEWS_FILE, postId);
        } catch (IOException e) {
            throw new SystemException("Error deleting post: " + e.getMessage());
        }
    }

    // metodo che rimuove gli elementi dal file
    private void deleteFromFile(String filename, int postId) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }

        List<String> linesToKeep = new ArrayList<>(); // lista che mantiene le righe che non devono essere eliminatw
        boolean headerSkipped = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // vengono lette tutte le linee del file
            while ((line = reader.readLine()) != null) {
                // blocco per riconoscere e non eliminare la riga di intestazione del file
                if (!headerSkipped && (line.contains("id|") || line.contains("post_id|") || line.contains("username|"))) {
                    linesToKeep.add(line);
                    headerSkipped = true;
                    continue;
                }

                if (!line.startsWith(postId + DELIMITER)) {
                    // se la linea non inizia con il postId passato al metodo, vuol dire che non è la riga da eliminare e viene aggiunta alla lista
                    linesToKeep.add(line);
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : linesToKeep) {
                // viene riscritto il file riga per riga con le righe che non devono essere cancellate e che erano salvate nella lista
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
