package org.example.ispwproject.model.observer.pokewall;

import org.example.ispwproject.utils.exception.SystemException;
import org.example.ispwproject.utils.database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBPokeWallDAO implements PokeWallDAO {
    @Override
    public void create(PokeWall pokeWall) throws SystemException {
        String insertPostSQL = "INSERT INTO poke_wall_posts (pokeName, size, username) VALUES (?, ?, ?) RETURNING id";
        String insertIngredientSQL = "INSERT INTO poke_wall_ingredients (post_id, ingredient) VALUES (?, ?)";

        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement postStmt = connection.prepareStatement(insertPostSQL);
             PreparedStatement ingredientStmt = connection.prepareStatement(insertIngredientSQL)) {

            // Salva il post e ottieni l'ID generato
            postStmt.setString(1, pokeWall.getPokeName());
            postStmt.setString(2, pokeWall.getSize());
            postStmt.setString(3, pokeWall.getUsername());
            ResultSet rs = postStmt.executeQuery();

            if (rs.next()) {
                int postId = rs.getInt(1);

                ingredientStmt.setInt(1, postId);
                for (String ingredient : pokeWall.getIngredients()) {
                    ingredientStmt.setString(2, ingredient);
                    ingredientStmt.addBatch();
                }

                ingredientStmt.executeBatch();
            }
        } catch (SQLException e) {
            throw new SystemException("Error saving post to database");
        }
    }


    @Override
    public List<PokeWall> getAllPosts() throws SystemException {
        List<PokeWall> posts = new ArrayList<>();
        String query = "SELECT p.id, p.pokeName, p.size, p.username, i.ingredient " +
                "FROM poke_wall_posts p " +
                "LEFT JOIN poke_wall_ingredients i ON p.id = i.post_id " +
                "ORDER BY p.id, i.id";

        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            Map<Integer, PokeWall> postMap = new HashMap<>();
            while (rs.next()) {
                int postId = rs.getInt("id");
                String pokeName = rs.getString("pokeName");
                String size = rs.getString("size");
                String username = rs.getString("username");
                String ingredient = rs.getString("ingredient");

                if (!postMap.containsKey(postId)) {
                    postMap.put(postId, new PokeWall(pokeName, size, username, new ArrayList<>()));
                }

                if (ingredient != null) {
                    postMap.get(postId).getIngredients().add(ingredient);
                }
            }

            posts.addAll(postMap.values());
        } catch (SQLException e) {
            throw new SystemException("Error retrieving posts from database");
        }

        return posts;
    }



    @Override
    public void delete(int postId) throws SystemException {
        String query = "DELETE FROM poke_wall_posts WHERE id = ?";  // Assicurati che la colonna si chiami 'id'
        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, postId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SystemException("Post with ID " + postId + " not found");
            }
        } catch (SQLException e) {
            throw new SystemException("Error deleting post: " + e.getMessage());
        }
    }
}
