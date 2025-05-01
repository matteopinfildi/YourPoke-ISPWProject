package org.example.ispwproject.model.user;

import org.example.ispwproject.model.decorator.pokelab.DBPokeLabDAO;
import org.example.ispwproject.model.decorator.pokelab.PokeLab;
import org.example.ispwproject.utils.enumeration.UserType;
import org.example.ispwproject.utils.exception.SystemException;
import org.example.ispwproject.utils.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUserDAO implements UserDAO{

    @Override
    public void create (User user) throws SystemException{

        String checkUserQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
        String insertUserQuery = "INSERT INTO users (username, password, email, utype, address, plid) VALUES (?, ?, ?, ?, ?, NULL)";
        try (Connection connection = DBConnection.getDBConnection(); PreparedStatement preparedStatementCheck = connection.prepareStatement(checkUserQuery); PreparedStatement preparedStatementInsert = connection.prepareStatement(insertUserQuery)){
            preparedStatementCheck.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatementCheck.executeQuery();
            resultSet.next();
            int counter = resultSet.getInt(1);

            if (counter == 0){
                preparedStatementInsert.setString(1, user.getUsername());
                preparedStatementInsert.setString(2, user.getPassword());
                preparedStatementInsert.setString(3, user.getEmail());
                preparedStatementInsert.setString(4, user.getuType().name());
                preparedStatementInsert.setString(5, user.getAddress());
                preparedStatementInsert.executeUpdate();
            }
        } catch (SQLException e) {
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
                // Leggi plid
                int plid = resultSet.getInt("plid");

                // Verifica che plid non sia nullo (o zero)
                PokeLab pokeLab = null;
                if (!resultSet.wasNull() && plid > 0) {
                    // Se plid è valido, prova a caricare il PokeLab
                    try {
                        pokeLab = new DBPokeLabDAO().read(plid);
                    } catch (SystemException e) {
                        // Se non riesci a trovare il PokeLab, logga l'errore o gestisci la situazione
                        System.err.println("Attenzione: PokeLab con plid " + plid + " non trovato.");
                    }
                }

                // Leggi i dati dell'utente
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                UserType userType = UserType.valueOf(resultSet.getString("utype"));
                String address = resultSet.getString("address");

                // Crea l'oggetto User
                User user = new User(username, password, email, userType, address);

                // Imposta il PokeLab, se presente
                user.setPokeLab(pokeLab);
                return user;
            } else {
                return null;  // L'utente non è stato trovato
            }
        } catch (SQLException e) {
            throw new SystemException("Errore nella lettura dell'utente");
        }
    }


//    @Override
//    public User read(String uid) throws SystemException {
//        String queryRead = "SELECT username, password, email, utype, address, plid FROM users WHERE username = ?";
//        try (Connection connection = DBConnection.getDBConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(queryRead)) {
//
//            preparedStatement.setString(1, uid);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                // MODIFICA CRUCIALE QUI:
//                int plid = resultSet.getInt("plid");
//                PokeLab pokeLab = null;
//
//                // Solo se plid è valido (non NULL e > 0)
//                if (!resultSet.wasNull() && plid > 0) {
//                    try {
//                        pokeLab = new DBPokeLabDAO().read(plid);
//                    } catch (SystemException e) {
//                        System.err.println("Attenzione: PokeLab " + plid + " non trovato, continuo senza");
//                        // Continua senza PokeLab invece di fallire
//                    }
//                }
//
//                String username = resultSet.getString("username");
//                String password = resultSet.getString("password");
//                String email = resultSet.getString("email");
//                UserType userType = UserType.valueOf(resultSet.getString("utype"));
//                String address = resultSet.getString("address");
//
//                User user = new User(username, password, email, userType, address);
//                user.setPokeLab(pokeLab);
//                return user;
//            } else {
//                return null;
//            }
//        } catch (SQLException e) {
//            throw new SystemException("Errore lettura utente " + uid);
//        }
//    }

//    @Override
//    public User read(String uid) throws SystemException{
//        String queryRead = "SELECT username, password, email, utype, address, plid FROM users WHERE username = ?";
//        try(Connection connection = DBConnection.getDBConnection(); PreparedStatement preparedStatement =connection.prepareStatement(queryRead)){
//            preparedStatement.setString(1, uid);
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()){
//                int plid = resultSet.getInt("plid");
//                System.out.println(plid);
//                PokeLab pokeLab = plid != -1 ? new DBPokeLabDAO().read(plid) : null;
//
//                String username = resultSet.getString("username");
//                String password = resultSet.getString("password");
//                String email = resultSet.getString("email");
//                UserType userType = UserType.valueOf(resultSet.getString("utype"));
//                String address = resultSet.getString("address");
//
//                User user = new User(username, password, email, userType, address);
//                user.setPokeLab(pokeLab);
//                return user;
//            }else{
//                return null;
//            }
//        }catch (SQLException e) {
//            throw new SystemException("Error read!");
//        }
//    }

    @Override
    public void update(User user, int plid) throws SystemException {
        String queryUpdate = "UPDATE users SET plid = ? WHERE username = ?";
        try (Connection connection = DBConnection.getDBConnection(); PreparedStatement preparedStatement = connection.prepareStatement(queryUpdate)) {
            deletePokeLab(user);
            preparedStatement.setInt(1, plid);
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException("Errore update!"+e.getMessage());
        }
    }

    private void deletePokeLab(User user) throws SystemException {
        String query = "UPDATE users SET plid = NULL WHERE username = ?";
        try (Connection connection = DBConnection.getDBConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException("Errore delete poke lab!"+ e.getMessage());
        }
    }

    @Override
    public void delete (String uid) throws SystemException {
        String queryDelete = "DELETE FROM users WHERE username = ?";
        try (Connection connection = DBConnection.getDBConnection(); PreparedStatement preparedStatement = connection.prepareStatement(queryDelete)){
            preparedStatement.setString(1, uid);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new SystemException("Errore delete user!");
        }
    }
}
