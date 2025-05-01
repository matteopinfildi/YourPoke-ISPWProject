package org.example.ispwproject.model.decorator.pokelab;

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
        String insertPokeLabQuery = "Insert into poke_lab (price, size) VALUES (?, ?)";
        String insertIngredientQuery = "Insert into poke_lab_ingredients (plid, ingredient_name, ingredient_alternative) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement preparedStatementPoke = connection.prepareStatement(insertPokeLabQuery, Statement.RETURN_GENERATED_KEYS)) {

            // Rimuoviamo il controllo per plid, perché lo recuperiamo dal database
            preparedStatementPoke.setDouble(1, pokeLab.price());
            preparedStatementPoke.setString(2, pokeLab.getBowlSize());

            // Eseguiamo l'inserimento di poke_lab
            int rowsAffected = preparedStatementPoke.executeUpdate();

            // Se l'inserimento è riuscito, otteniamo l'ID generato
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatementPoke.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int plid = generatedKeys.getInt(1);  // Otteniamo l'ID generato
                        // Inseriamo gli ingredienti nel batch
                        try (PreparedStatement preparedStatementIngredient = connection.prepareStatement(insertIngredientQuery)) {
                            for (Map.Entry<String, GenericAlternative> entry : pokeLab.allIngredients().entrySet()) {
                                preparedStatementIngredient.setInt(1, plid);
                                preparedStatementIngredient.setString(2, entry.getKey());
                                preparedStatementIngredient.setString(3, ((Enum<?>)entry.getValue()).name());

                                preparedStatementIngredient.addBatch();
                            }

                            preparedStatementIngredient.executeBatch();
                        }
                    } else {
                        throw new SystemException("Failed to retrieve the generated PokeLab ID");
                    }
                }
            } else {
                throw new SystemException("Failed to insert PokeLab");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error while creating PokeLab entry", e);
            throw new SystemException("Error creating PokeLab in database");
        }
    }

    @Override
    public PokeLab read(int plid) throws SystemException {
        String selectPokeLabQuery = "SELECT price, size FROM poke_lab WHERE id = ?";
        String selectIngredientQuery = "SELECT ingredient_name, ingredient_alternative FROM poke_lab_ingredients WHERE plid = ?";

        try (Connection connection = DBConnection.getDBConnection(); PreparedStatement preparedStatementPoke = connection.prepareStatement(selectPokeLabQuery); PreparedStatement preparedStatementIngredient = connection.prepareStatement(selectIngredientQuery)){
            preparedStatementPoke.setInt(1, plid);
            ResultSet resultSetPoke = preparedStatementPoke.executeQuery();

            if (resultSetPoke.next()){
                double price = resultSetPoke.getDouble("price");
                String size = resultSetPoke.getString("size");
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

                return new PokeLab(price, plid, ingredients, size);
            }
        }catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error while reading PokeLab entry with ID ");
            throw new SystemException("Error reading PokeLab from database");
        }
        return null;
    }

    @Override
    public void delete(int plid) throws SystemException {
        String deletePokeQuery = "DELETE FROM poke_lab WHERE id = ?";

        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deletePokeQuery)) {

            preparedStatement.setInt(1, plid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error while deleting PokeLab entry with ID ");
            throw new SystemException("Error deleting PokeLab from database");
        }
    }

    @Override
    public void updateBowlSize(int plid, String bowlSize) throws SystemException {
        String query = "UPDATE poke_lab SET size = ? WHERE id = ?";

        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, bowlSize);
            preparedStatement.setInt(2, plid);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("Nessun record trovato con ID: " + plid);
            } else {
                System.out.println("Bowl size aggiornato per ID: " + plid);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error while updating bowl size for PokeLab with ID " );
            throw new SystemException("Error updating PokeLab bowl size");
        }
    }


}
