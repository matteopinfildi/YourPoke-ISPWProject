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
            connection.commit();
        } catch (SQLException | SystemException e) {
            try {
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
        int postId = insertPostAndReturnId(connection, pokeWall);
        insertIngredients(connection, postId, pokeWall.getIngredients());
    }


    private int insertPostAndReturnId(Connection connection, PokeWall pokeWall) throws SQLException, SystemException {
        String insertPostSQL = "INSERT INTO poke_wall_posts (poke_name, size, username) VALUES (?, ?, ?)";
        try (PreparedStatement postStmt = connection.prepareStatement(insertPostSQL, Statement.RETURN_GENERATED_KEYS)) {
            postStmt.setString(1, pokeWall.getPokeName());
            postStmt.setString(2, pokeWall.getSize());
            postStmt.setString(3, pokeWall.getUsername());
            int rowsAffected = postStmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SystemException("Impossibile inserire il post. Nessuna riga influenzata.");
            }
            try (ResultSet rs = postStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int postId = rs.getInt(1);
                    return postId;
                } else {
                    throw new SystemException("Impossibile ottenere l'ID del post generato.");
                }
            }
        }
    }

    private void insertIngredients(Connection connection, int postId, List<String> ingredients) throws SQLException {
        String insertIngredientSQL = "INSERT INTO poke_wall_ingredients (post_id, ingredient) VALUES (?, ?)";
        try (PreparedStatement ingredientStmt = connection.prepareStatement(insertIngredientSQL)) {
            ingredientStmt.setInt(1, postId);
            for (String ingredient : ingredients) {
                ingredientStmt.setString(2, ingredient);
                ingredientStmt.addBatch();
            }
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
            Map<Integer, PokeWall> postMap = new LinkedHashMap<>();
            while (rs.next()) {
                int postId = rs.getInt("id");
                String pokeName = rs.getString("poke_name");
                String size = rs.getString("size");
                String username = rs.getString("username");
                String ingredient = rs.getString("ingredient");

                // Passa postId come primo parametro, seguito dagli altri parametri
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
            ps.setInt(1, postId);
            if (ps.executeUpdate() == 0) {
                throw new SystemException("Post con ID " + postId + " non trovato");
            }
        } catch (SQLException e) {
            throw new SystemException("Errore durante l'eliminazione: " + e.getMessage());
        }
    }




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
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer, PokeWall> postMap = new LinkedHashMap<>();
                while (rs.next()) {
                    int postId = rs.getInt("id");
                    String pokeName = rs.getString("poke_name");
                    String size = rs.getString("size");
                    String postUsername = rs.getString("username");
                    String ingredient = rs.getString("ingredient");

                    // Passa 5 argomenti: postId, pokeName, size, postUsername, e lista di ingredienti vuota
                    postMap.computeIfAbsent(postId, k -> new PokeWall(postId, pokeName, size, postUsername, new ArrayList<>()));

                    if (ingredient != null) {
                        postMap.get(postId).getIngredients().add(ingredient);
                    }
                }

                unseenPosts.addAll(postMap.values());
            }
        } catch (SQLException e) {
            throw new SystemException("Errore nel recupero dei post non visti: " + e.getMessage());
        }

        return unseenPosts;
    }


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

            stmt.setInt(1, postId);
            stmt.setString(2, username);


        } catch (SQLException e) {
            throw new SystemException("Errore in markPostAsSeen: " + e.getMessage());
        }
    }

}
