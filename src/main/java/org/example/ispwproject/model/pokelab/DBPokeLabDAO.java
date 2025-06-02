package org.example.ispwproject.model.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.*;
import org.example.ispwproject.utils.exception.SystemException;
import org.example.ispwproject.utils.database.DBConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBPokeLabDAO implements PokeLabDAO {
    private static final Logger logger = Logger.getLogger(DBPokeLabDAO.class.getName());

    @Override
    public void create(PokeLab pokeLab) throws SystemException {
        String insertPokeLabQuery = "INSERT INTO poke_lab (price, size, calories) VALUES (?, ?, ?)";
        String insertIngredientQuery = "INSERT INTO poke_lab_ingredients (plid, ingredient_name, ingredient_alternative) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement preparedStatementPoke = connection.prepareStatement(
                     insertPokeLabQuery, Statement.RETURN_GENERATED_KEYS)) {

            // 1. Inserisci il PokeLab principale
            preparedStatementPoke.setDouble(1, pokeLab.price());
            preparedStatementPoke.setString(2, pokeLab.getBowlSize());
            preparedStatementPoke.setInt(3, pokeLab.calories());
            preparedStatementPoke.executeUpdate();

            // 2. Ottieni l'ID generato
            try (ResultSet generatedKeys = preparedStatementPoke.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int plid = generatedKeys.getInt(1);
                    pokeLab.setId(plid);

                    // 3. Inserisci gli ingredienti
                    try (PreparedStatement stmtIngredients = connection.prepareStatement(insertIngredientQuery)) {
                        stmtIngredients.setInt(1, plid);
                        for (Map.Entry<String, GenericAlternative> entry : pokeLab.allIngredients().entrySet()) {
                            stmtIngredients.setString(2, entry.getKey());
                            stmtIngredients.setString(3, ((Enum<?>) entry.getValue()).name());
                            stmtIngredients.addBatch();
                        }
                        stmtIngredients.executeBatch();
                    }
                }
            }
        } catch (SQLException e) {
            throw new SystemException("Error creating PokeLab: " + e.getMessage());
        }
    }

    @Override
    public PokeLab read(int plid) throws SystemException {
        String selectPokeLabQuery = "SELECT price, size, calories FROM poke_lab WHERE id = ?";
        String selectIngredientQuery = "SELECT ingredient_name, ingredient_alternative FROM poke_lab_ingredients WHERE plid = ?";

        try (Connection connection = DBConnection.getDBConnection(); PreparedStatement preparedStatementPoke = connection.prepareStatement(selectPokeLabQuery); PreparedStatement preparedStatementIngredient = connection.prepareStatement(selectIngredientQuery)){
            preparedStatementPoke.setInt(1, plid);
            ResultSet resultSetPoke = preparedStatementPoke.executeQuery();

            if (resultSetPoke.next()){
                double price = resultSetPoke.getDouble("price");
                String size = resultSetPoke.getString("size");
                int calories = resultSetPoke.getInt("calories");
                Map<String, GenericAlternative> ingredients =new HashMap<>();

                preparedStatementIngredient.setInt(1, plid);
                ResultSet resultSetIngredient = preparedStatementIngredient.executeQuery();

                while(resultSetIngredient.next()){
                    String ingredientName = resultSetIngredient.getString("ingredient_name");
                    String ingredientAlternative = resultSetIngredient.getString("ingredient_alternative");

                    GenericAlternative genericAlternative = null;
                    switch (ingredientName){
                        case "rice" -> genericAlternative = Enum.valueOf(RiceAlternative.class, ingredientAlternative);
                        case "protein" -> genericAlternative = Enum.valueOf(ProteinAlternative.class, ingredientAlternative);
                        case "fruit" -> genericAlternative = Enum.valueOf(FruitAlternative.class, ingredientAlternative);
                        case "crunchy" -> genericAlternative = Enum.valueOf(CrunchyAlternative.class, ingredientAlternative);
                        case "sauces" -> genericAlternative = Enum.valueOf(SaucesAlternative.class, ingredientAlternative);
                        default -> {
                            //non succede nulla
                        }
                    }
                    if (genericAlternative != null){
                        ingredients.put(ingredientName, genericAlternative);
                    }
                }

                return new PokeLab(price, plid, ingredients, size, calories);
            }
        }catch (SQLException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, String.format("SQL error while reading PokeLab entry with ID %d", plid), e);
            }

            throw new SystemException("Error reading PokeLab from database");
        }
        return null;
    }

    @Override
    public void delete(int plid) throws SystemException {
        // Prima elimina gli ingredienti (figli) e poi il PokeLab (padre)
        String deleteIngredientsQuery = "DELETE FROM poke_lab_ingredients WHERE plid = ?";
        String deletePokeLabQuery = "DELETE FROM poke_lab WHERE id = ?";

        try (Connection connection = DBConnection.getDBConnection()) {
            // Disabilita temporaneamente i vincoli di foreign key
            try (Statement disableFK = connection.createStatement()) {
                disableFK.execute("SET FOREIGN_KEY_CHECKS=0");
            }

            // 1. Elimina prima gli ingredienti associati
            try (PreparedStatement stmtIngredients = connection.prepareStatement(deleteIngredientsQuery)) {
                stmtIngredients.setInt(1, plid);
                stmtIngredients.executeUpdate();
            }

            // 2. Poi elimina il PokeLab
            try (PreparedStatement stmtPokeLab = connection.prepareStatement(deletePokeLabQuery)) {
                stmtPokeLab.setInt(1, plid);
                int rowsAffected = stmtPokeLab.executeUpdate();

                if (rowsAffected == 0) {
                    throw new SystemException("No PokeLab found with ID: " + plid);
                }
            }

            // Riabilita i vincoli di foreign key
            try (Statement enableFK = connection.createStatement()) {
                enableFK.execute("SET FOREIGN_KEY_CHECKS=1");
            }

        } catch (SQLException e) {
            throw new SystemException("Error deleting PokeLab from database: " + e.getMessage());
        }
    }



    @Override
    public void update(PokeLab pokeLab) throws SystemException {
        String updatePokeLab = "UPDATE poke_lab SET price = ?, size = ?, calories = ? WHERE id = ?";
        String deleteIngredients = "DELETE FROM poke_lab_ingredients WHERE plid = ?";
        String insertIngredient = "INSERT INTO poke_lab_ingredients (plid, ingredient_name, ingredient_alternative) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getDBConnection()) {
            // 1) aggiorna price e size
            try (PreparedStatement ps = conn.prepareStatement(updatePokeLab)) {
                ps.setDouble(1, pokeLab.price());
                ps.setString(2, pokeLab.getBowlSize());
                ps.setInt(3, pokeLab.calories());
                ps.setInt(4, pokeLab.id());
                ps.executeUpdate();
            }
            // 2) elimina tutte le righe ingredienti esistenti
            try (PreparedStatement ps = conn.prepareStatement(deleteIngredients)) {
                ps.setInt(1, pokeLab.id());
                ps.executeUpdate();
            }
            // 3) reinserisci gli ingredienti correnti
            try (PreparedStatement ps = conn.prepareStatement(insertIngredient)) {
                for (var e : pokeLab.allIngredients().entrySet()) {
                    ps.setInt(1, pokeLab.id());
                    ps.setString(2, e.getKey());
                    ps.setString(3, ((Enum<?>) e.getValue()).name());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        } catch (SQLException ex) {
            throw new SystemException("Error updating PokeLab: " + ex.getMessage());
        }
    }

}
