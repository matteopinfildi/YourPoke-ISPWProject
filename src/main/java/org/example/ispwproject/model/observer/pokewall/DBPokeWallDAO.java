package org.example.ispwproject.model.observer.pokewall;

import org.example.ispwproject.utils.database.DBConnection;
import org.example.ispwproject.utils.exception.SystemException;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class DBPokeWallDAO implements PokeWallDAO {

    private static final Logger logger = Logger.getLogger(DBPokeWallDAO.class.getName());

    @Override
    public void create(PokeWall pokeWall) throws SystemException {
        // apriamo una connessione al db
        try (Connection connection = DBConnection.getDBConnection()) {
            connection.setAutoCommit(false);
            executeCreateTransaction(connection, pokeWall);
        } catch (SQLException e) {
            throw new SystemException("Errore di connessione al database: " + e.getMessage());
        }
    }

    private void executeCreateTransaction(Connection connection, PokeWall pokeWall) throws SystemException {
        try {
            performCreateTransaction(connection, pokeWall);
            // se il metodo richiamato non ha lanciato eccezioni, viene eseguito il commit
            connection.commit();
        } catch (SQLException | SystemException e) {
            try {
                // vengono annullate tutte le operazioni fatte sulla connessione fino a questo momento
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.severe("Errore durante il rollback: " + rollbackEx.getMessage());
            }
            throw new SystemException("Errore durante la creazione del post: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                logger.severe("Impossibile ripristinare autoCommit: " + autoCommitEx.getMessage());
            }
        }
    }


    private void performCreateTransaction(Connection connection, PokeWall pokeWall) throws SQLException, SystemException {
        // richiama un metodo che effettua l'insert e che restituisce l'id del post creato
        int postId = insertPostAndReturnId(connection, pokeWall);
        // il valore dell'id del post verrà utilizzato per inserire gli ingredienti
        insertIngredients(connection, postId, pokeWall.getIngredients());
    }


    private int insertPostAndReturnId(Connection connection, PokeWall pokeWall) throws SQLException, SystemException {
        String insertPostSQL = "INSERT INTO poke_wall_posts (poke_name, size, username) VALUES (?, ?, ?)";
        try (PreparedStatement postStmt = connection.prepareStatement(insertPostSQL, Statement.RETURN_GENERATED_KEYS)) {
            postStmt.setString(1, pokeWall.getPokeName()); // imposta il primo parametro della query con il nome del post
            postStmt.setString(2, pokeWall.getSize()); // imposta il primo parametro della query con la size del poke
            postStmt.setString(3, pokeWall.getUsername()); // imposta il primo parametro della query con lo username del creatore
            // viene eseguito l'insert effettivo e controllo se va a buon fine
            int rowsAffected = postStmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SystemException("Impossibile inserire il post. Nessuna riga influenzata.");
            }
            try (ResultSet rs = postStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SystemException("Impossibile ottenere l'ID del post generato.");
                }
            }
        }
    }

    private void insertIngredients(Connection connection, int postId, List<String> ingredients) throws SQLException {
        String insertIngredientSQL = "INSERT INTO poke_wall_ingredients (post_id, ingredient) VALUES (?, ?)";
        try (PreparedStatement ingredientStmt = connection.prepareStatement(insertIngredientSQL)) {
            ingredientStmt.setInt(1, postId); // il primo parametro viene impostato con il valore del post id
            // vengono inseriti tutti gli ingredienti
            for (String ingredient : ingredients) {
                ingredientStmt.setString(2, ingredient);
                // questo comando non fa effettuare subito l'insert, ma accumula tutto per farne uno solo
                ingredientStmt.addBatch();
            }
            // vengono eseguiti in sequenza tutti gli insert accomulati
            ingredientStmt.executeBatch();
        }
    }



    @Override
    public List<PokeWall> getAllPosts() throws SystemException {
        String query = "SELECT p.id, p.poke_name, p.size, p.username, i.ingredient " +
                "FROM poke_wall_posts p " +
                "LEFT JOIN poke_wall_ingredients i ON p.id = i.post_id " +
                "ORDER BY p.id, i.id";

        List<PokeWall> posts = new ArrayList<>();

        try (
                Connection connection = DBConnection.getDBConnection();
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()
        ) {
            // mappa che associa a ciascun id del post, un post effettivo
            Map<Integer, PokeWall> postMap = new LinkedHashMap<>();
            while (rs.next()) {
                // vengono lette le varie colonne e vengono assegnati i valori a delle variabili
                int postId = rs.getInt("id");
                String pokeName = rs.getString("poke_name");
                String size = rs.getString("size");
                String username = rs.getString("username");
                String ingredient = rs.getString("ingredient");

                // se non esiste ancora un post associato a quell'id viene creato con new PokeWall(...)
                postMap.computeIfAbsent(postId, k -> new PokeWall(postId, pokeName, size, username, new ArrayList<>()));
                if (ingredient != null) {
                    postMap.get(postId).getIngredients().add(ingredient);
                }
            }
            posts.addAll(postMap.values());
        } catch (SQLException e) {
            throw new SystemException("Errore nel recupero post: " + e.getMessage());
        }

        return posts;
    }


    @Override
    public void delete(int postId) throws SystemException {
        String query = "DELETE FROM poke_wall_posts WHERE id = ?";

        try (
                Connection connection = DBConnection.getDBConnection();
                PreparedStatement ps = connection.prepareStatement(query)
        ) {
            // viene impostato il parametro della query con il valore del postId passato al metodo
            ps.setInt(1, postId);
            if (ps.executeUpdate() == 0) {
                throw new SystemException("Post con ID " + postId + " non trovato");
            }
        } catch (SQLException e) {
            throw new SystemException("Errore durante l'eliminazione: " + e.getMessage());
        }
    }



    // serve a restituire tutti i post che un utente non ha ancora visualizzato
    @Override
    public List<PokeWall> getUnseenPosts(String username) throws SystemException {
        List<PokeWall> unseenPosts = new ArrayList<>();
        String query = """
    SELECT p.id, p.poke_name, p.size, p.username, i.ingredient
    FROM poke_wall_posts p
    LEFT JOIN poke_wall_ingredients i ON p.id = i.post_id
    WHERE NOT EXISTS (
        SELECT 1 FROM poke_post_views ppv
        WHERE ppv.post_id = p.id AND ppv.username = ?
    )
    ORDER BY p.id, i.id
    """;

        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // viene impostato il primo valore della query con lo username passato al metodo
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                // mappa di post non ancora visualizzati
                Map<Integer, PokeWall> postMap = new LinkedHashMap<>();
                while (rs.next()) {
                    // vengono lette tutte le colonne dei post che scorrono nel ciclo while
                    int postId = rs.getInt("id");
                    String pokeName = rs.getString("poke_name");
                    String size = rs.getString("size");
                    String postUsername = rs.getString("username");
                    String ingredient = rs.getString("ingredient");

                    // se non esiste ancora un post associato a quell'id viene creato con new PokeWall(...)
                    postMap.computeIfAbsent(postId, k -> new PokeWall(postId, pokeName, size, postUsername, new ArrayList<>()));

                    if (ingredient != null) {
                        postMap.get(postId).getIngredients().add(ingredient);
                    }
                }

                // aggiunge tutti i PokeWall raggruppati nella mappa alla lista unseenPosts.
                unseenPosts.addAll(postMap.values());
            }
        } catch (SQLException e) {
            throw new SystemException("Errore nel recupero dei post non visti: " + e.getMessage());
        }

        return unseenPosts;
    }


    // un post viene segnato come già visualizzato da uno user
    public void markPostAsSeen(int postId, String username) throws SystemException {
        if (postId <= 0) {
            throw new IllegalArgumentException("postId deve essere maggiore di 0");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username non può essere null o vuoto");
        }

        String query = "INSERT IGNORE INTO poke_post_views (post_id, username) VALUES (?, ?)";
        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, postId); // primo valore della query impostato con l'id del post
            stmt.setString(2, username); // primo valore della query impostato con lo username del creatore del post
            stmt.executeUpdate(); // viene eseguito l'insert


        } catch (SQLException e) {
            throw new SystemException("Errore in markPostAsSeen: " + e.getMessage());
        }
    }

}
