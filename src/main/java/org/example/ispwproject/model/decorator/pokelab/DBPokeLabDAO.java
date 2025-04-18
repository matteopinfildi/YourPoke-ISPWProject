package org.example.ispwproject.model.decorator.pokelab;

import org.example.ispwproject.utils.enumeration.ingredient.*;
import org.example.ispwproject.utils.exception.SystemException;
import org.example.ispwproject.utils.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBPokeLabDAO implements PokeLabDAO {
    private static final Logger logger = Logger.getLogger(DBPokeLabDAO.class.getName());

    @Override
    public void create(PokeLab pokeLab) throws SystemException {
        String insertPokeLabQuery = "Insert into pokeLab (id, price, size) VALUES (?, ?, ?)";
        String insertIngredientQuery = "Insert into pokeLabIngredients (plid, ingredientName, ingredientAlternative) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getDBConnection(); PreparedStatement preparedStatementPoke = connection.prepareStatement(insertPokeLabQuery)){
            int plid = pokeLab.id();
            if(plid == -1){
                throw new SystemException("The pokè lab id is null");
            }

            preparedStatementPoke.setInt(1, plid);
            preparedStatementPoke.setDouble(2, pokeLab.price());
            preparedStatementPoke.setString(3, pokeLab.getBowlSize());
            preparedStatementPoke.executeUpdate();

            try (PreparedStatement preparedStatementIngredient = connection.prepareStatement(insertIngredientQuery)){
                preparedStatementIngredient.setInt(1, plid);
                for (Map.Entry<String, GenericAlternative> entry : pokeLab.allIngredients().entrySet()){
                    preparedStatementIngredient.setString(2, entry.getKey());
                    preparedStatementIngredient.setString(3, ((Enum<?>)entry.getValue()).name());

                    preparedStatementIngredient.addBatch();
                }

                preparedStatementIngredient.executeBatch();
            }
        }catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error while creating PokeLab entry", e);
            throw new SystemException("Error creating PokeLab in database");
        }
    }

    @Override
    public PokeLab read(int plid) throws SystemException {
        String selectPokeLabQuery = "SELECT price FROM pokeLab WHERE id = ?";
        String selectIngredientQuery = "SELECT ingredientName, ingredientAlternative FROM pokeLabIngredients WHERE plid = ?";

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
                    String ingredientName = resultSetIngredient.getString("ingredientName");
                    String ingredientAlternative = resultSetIngredient.getString("ingredientAlternative");

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
        String deletePokeQuery = "DELETE FROM pokeLab WHERE id = ?";

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
        String query = "UPDATE pokeLab SET bowl_size = ? WHERE id = ?";

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
