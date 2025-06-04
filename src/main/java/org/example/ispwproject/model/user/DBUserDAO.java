package org.example.ispwproject.model.user;

import org.example.ispwproject.model.pokelab.DBPokeLabDAO;
import org.example.ispwproject.model.pokelab.PokeLab;
import org.example.ispwproject.utils.enumeration.UserType;
import org.example.ispwproject.utils.exception.SystemException;
import org.example.ispwproject.utils.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBUserDAO implements UserDAO{

    private static final Logger LOGGER = Logger.getLogger(DBUserDAO.class.getName());


    @Override
    public void create (User user) throws SystemException{

        String checkUserQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
        String insertUserQuery = "INSERT INTO users (username, password, email, utype, address, plid) VALUES (?, ?, ?, ?, ?, NULL)";
        try (Connection connection = DBConnection.getDBConnection(); PreparedStatement preparedStatementCheck = connection.prepareStatement(checkUserQuery); PreparedStatement preparedStatementInsert = connection.prepareStatement(insertUserQuery)){
            preparedStatementCheck.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatementCheck.executeQuery();
            resultSet.next();
            int counter = resultSet.getInt(1); // rappresenta quante righe con quello username esistono già

            if (counter == 0){
                // se le righe con quello username non esistono, allora vengono inseriti tutti i valori relativi ad uno user
                preparedStatementInsert.setString(1, user.getUsername());
                preparedStatementInsert.setString(2, user.getPassword());
                preparedStatementInsert.setString(3, user.getEmail());
                preparedStatementInsert.setString(4, user.getuType().name());
                preparedStatementInsert.setString(5, user.getAddress());
                preparedStatementInsert.executeUpdate();
            }
        } catch (SQLException _) {
            throw new SystemException("Error create!");
        }
    }


    @Override
    public User read(String uid) throws SystemException {
        String queryRead = "SELECT username, password, email, utype, address, plid FROM users WHERE username = ?";

        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryRead)) {

            preparedStatement.setString(1, uid);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // prende il valore del plid dal db e lo associa ad una variabile
                int plid = resultSet.getInt("plid");

                // recupera il PokeLab in un metodo separato
                PokeLab pokeLab = fetchPokeLab(plid);

                // prende i valori dei dati dell'utente e li associa a delle variabili
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                UserType userType = UserType.valueOf(resultSet.getString("utype"));
                String address = resultSet.getString("address");

                // costruisce un nuovo oggetto user, passandogli i parametri recuperati in precedenza
                User user = new User(username, password, email, userType, address);

                // associa il poke lab allo user
                user.setPokeLab(pokeLab);
                return user; // ritorna lo user
            } else {
                return null;  // lo user non è stato trovato
            }
        } catch (SQLException _) {
            throw new SystemException("Errore nella lettura dell'utente");
        }
    }

    // blocco di codice che recupera il PokeLab
    private PokeLab fetchPokeLab(int plid) throws SystemException {
        // verifica che plid non sia nullo
        if (plid > 0) {
            try {
                // se il plid è valido, allora viene instanziato un oggetto che restituisce un poke lab associato all'utente
                return new DBPokeLabDAO().read(plid);
            } catch (SystemException _) {
                LOGGER.warning("Attenzione: PokeLab con plid " + plid + " non trovato.");
                return null;  // Nessun PokeLab trovato, ma non lanciare eccezioni
            }
        }
        return null;  // plid non valido, quindi non c'è un PokeLab associato
    }



    // metodo che permette d aggiornare il valore di un plid associato ad uno user
    @Override
    public void update(User user, int plid) throws SystemException {
        String checkPokeLabQuery = "SELECT COUNT(*) FROM poke_lab WHERE id = ?";
        String updateUserQuery = "UPDATE users SET plid = ? WHERE username = ?";

        try (Connection connection = DBConnection.getDBConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkPokeLabQuery);
             PreparedStatement updateStmt = connection.prepareStatement(updateUserQuery)) {

            // si contano quanti poke lab hanno l'id passato al metodo (quindi controlliamo se esiste)
            checkStmt.setInt(1, plid);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                throw new SystemException("PokeLab with ID " + plid + " does not exist");
            }

            updateStmt.setInt(1, plid); // aggiorna il valore del plid
            updateStmt.setString(2, user.getUsername()); // imposta il valore dello username a cui è stato aggiornato il plid

            // viene eseguuito l’update su users, assegnando plid al record corrispondente a username
            int affectedRows = updateStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SystemException("No user found with username: " + user.getUsername());
            }
        } catch (SQLException e) {
            throw new SystemException("Error updating user's poke lab reference: " + e.getMessage());
        }
    }


    @Override
    public void delete (String uid) throws SystemException {
        String queryDelete = "DELETE FROM users WHERE username = ?";
        try (Connection connection = DBConnection.getDBConnection(); PreparedStatement preparedStatement = connection.prepareStatement(queryDelete)){
            preparedStatement.setString(1, uid); // imposta il parametro della query con l'id dello user da eliminare (che viene passato al metodo)
            preparedStatement.executeUpdate();
        }catch (SQLException _) {
            throw new SystemException("Errore delete user!");
        }
    }
}
