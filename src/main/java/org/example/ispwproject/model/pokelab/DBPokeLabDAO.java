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

            // vengono assegnati i valori alla query per il prezzo, la size e le calorie ottenute dal PokeLab
            preparedStatementPoke.setDouble(1, pokeLab.price());
            preparedStatementPoke.setString(2, pokeLab.getBowlSize());
            preparedStatementPoke.setInt(3, pokeLab.calories());
            preparedStatementPoke.executeUpdate();

            // generatedKeys contiene i valori automatici generati dall'insert
            try (ResultSet generatedKeys = preparedStatementPoke.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // viene letto l'id generato e viene assegnato alla variabile plid
                    int plid = generatedKeys.getInt(1);
                    // viene settato l'id all'oggetto pokeLab
                    pokeLab.setId(plid);

                    // vengono inseriti anche gli ingredienti, sfruttando il plid generato prima
                    try (PreparedStatement stmtIngredients = connection.prepareStatement(insertIngredientQuery)) {
                        stmtIngredients.setInt(1, plid);
                        // si sfrutta un ciclo for per inserire tutti gli ingredienti
                        for (Map.Entry<String, GenericOption> entry : pokeLab.allIngredients().entrySet()) {
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
            preparedStatementPoke.setInt(1, plid); // viene impostato il valore della prima query con il valore plid passato al metodo
            ResultSet resultSetPoke = preparedStatementPoke.executeQuery();

            if (resultSetPoke.next()){
                // vengono letti tutti i valori dal db e vengono associati a delle variabili
                double price = resultSetPoke.getDouble("price");
                String size = resultSetPoke.getString("size");
                int calories = resultSetPoke.getInt("calories");
                Map<String, GenericOption> ingredients =new HashMap<>();

                preparedStatementIngredient.setInt(1, plid); // viene impostato il valore della seconda query con il valore plid passato al metodo
                ResultSet resultSetIngredient = preparedStatementIngredient.executeQuery();

                while(resultSetIngredient.next()){
                    // associa i valori per ogni ingrediente e ogni alternativa lette sul db ad una variabile
                    String ingredientName = resultSetIngredient.getString("ingredient_name");
                    String ingredientAlternative = resultSetIngredient.getString("ingredient_alternative");

                    GenericOption genericOption = null;
                    // usiamo uno switch sul nome dell'ingrediente per determinare a quale classe di enum corrisponde l'alternativa
                    switch (ingredientName){
                        case "rice" -> genericOption = Enum.valueOf(RiceOption.class, ingredientAlternative);
                        case "protein" -> genericOption = Enum.valueOf(ProteinOption.class, ingredientAlternative);
                        case "fruit" -> genericOption = Enum.valueOf(FruitOption.class, ingredientAlternative);
                        case "crunchy" -> genericOption = Enum.valueOf(CrunchyOption.class, ingredientAlternative);
                        case "sauces" -> genericOption = Enum.valueOf(SaucesOption.class, ingredientAlternative);
                        default -> {
                            //non succede nulla
                        }
                    }
                    if (genericOption != null){
                        // se il valore dell'enum è stato creato correttamente, viene messo nella mappa ingredients
                        ingredients.put(ingredientName, genericOption);
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
        // prima elimina gli ingredienti (figli) e poi il PokeLab (padre)
        String deleteIngredientsQuery = "DELETE FROM poke_lab_ingredients WHERE plid = ?";
        String deletePokeLabQuery = "DELETE FROM poke_lab WHERE id = ?";

        try (Connection connection = DBConnection.getDBConnection()) {
            // disabilita temporaneamente i vincoli di foreign key
            try (Statement disableFK = connection.createStatement()) {
                disableFK.execute("SET FOREIGN_KEY_CHECKS=0");
            }

            // vengono eliminati prima gli ingredienti associati ad un poke lab
            try (PreparedStatement stmtIngredients = connection.prepareStatement(deleteIngredientsQuery)) {
                stmtIngredients.setInt(1, plid);
                stmtIngredients.executeUpdate();
            }

            // successivamente viene eliminato il poke lab
            try (PreparedStatement stmtPokeLab = connection.prepareStatement(deletePokeLabQuery)) {
                stmtPokeLab.setInt(1, plid);
                int rowsAffected = stmtPokeLab.executeUpdate();

                if (rowsAffected == 0) {
                    throw new SystemException("No PokeLab found with ID: " + plid);
                }
            }

            // riabilita i vincoli di foreign key
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
            // vengono aggiornati price, size e calories per un determinato poke lab
            try (PreparedStatement ps = conn.prepareStatement(updatePokeLab)) {
                ps.setDouble(1, pokeLab.price());
                ps.setString(2, pokeLab.getBowlSize());
                ps.setInt(3, pokeLab.calories());
                ps.setInt(4, pokeLab.id());
                ps.executeUpdate();
            }
            // vengono eliminate tutte le righe per gli ingredienti esistenti
            try (PreparedStatement ps = conn.prepareStatement(deleteIngredients)) {
                ps.setInt(1, pokeLab.id());
                ps.executeUpdate();
            }
            // vengono reinseriti gli ingredienti correnti (per cui quelli aggiornati)
            try (PreparedStatement ps = conn.prepareStatement(insertIngredient)) {
                for (var e : pokeLab.allIngredients().entrySet()) {
                    ps.setInt(1, pokeLab.id());
                    ps.setString(2, e.getKey());
                    ps.setString(3, ((Enum<?>) e.getValue()).name());
                    ps.addBatch();// accumula tutte le insert
                }
                ps.executeBatch(); // esegue tutte le insert accumulate
            }
        } catch (SQLException ex) {
            throw new SystemException("Error updating PokeLab: " + ex.getMessage());
        }
    }

}
