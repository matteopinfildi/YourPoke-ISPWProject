package org.example.ispwproject.model.user;

import org.example.ispwproject.model.decorator.pokelab.DBPokeLabDAO;
import org.example.ispwproject.model.decorator.pokelab.PokeLab;
import org.example.ispwproject.utils.enumeration.UserType;
import org.example.ispwproject.utils.exception.SystemException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FSUserDAO implements UserDAO{
    private static final String FILE_PATH = "user.csv";
    private static final String DELIMITER = ",";

    @Override
    public void create(User userA) throws SystemException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_PATH));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            // Controlla se l'utente esiste già
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(DELIMITER);
                if (data[0].equals(userA.getUsername())) {
                    return; // Utente già esistente, non aggiungerlo
                }
            }

            // Scrive l'utente nel file
            bufferedWriter.write(userA.getUsername() + DELIMITER +
                    userA.getPassword() + DELIMITER +
                    userA.getEmail() + DELIMITER +
                    userA.getuType().name() + DELIMITER +
                    userA.getAddress() + DELIMITER +
                    "-1" + "\n"); // con il -1 indichiamo che non c'è nessun pokè lab
        } catch (IOException e) {
            throw new SystemException("Failed to create user in FS");
        }
    }

    @Override
    public User read(String uId) throws SystemException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(DELIMITER);
                if (data[0].equals(uId)) {
                    int plid = Integer.parseInt(data[5]);
                    PokeLab pokeLab = (plid != -1) ? new DBPokeLabDAO().read(plid) : null;

                    User user = new User(data[0], data[1], data[2], UserType.valueOf(data[3]), data[4]);
                    user.setPokeLab(pokeLab);
                    return user;
                }
            }
        } catch (IOException e) {
            throw new SystemException("Failed to read user in FS");
        }
        return null;
    }

    @Override
    public void update(User userA, int plid) throws SystemException {
        List<String> users = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(DELIMITER);
                if (data[0].equals(userA.getUsername())) {
                    data[5] = String.valueOf(plid);
                    updated = true;
                }
                users.add(String.join(DELIMITER, data));
            }
        } catch (IOException e) {
            throw new SystemException("Failed to read users in FS");
        }

        if (!updated) return; // L'utente non esiste, nessun aggiornamento

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String user : users) {
                bufferedWriter.write(user + "\n");
            }
        } catch (IOException e) {
            throw new SystemException("EFailed to update user in FS");
        }
    }

    @Override
    public void delete(String userId) throws SystemException {
        List<String> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(DELIMITER);
                if (!data[0].equals(userId)) {
                    users.add(line);
                }
            }
        } catch (IOException e) {
            throw new SystemException("Failed to read user in FS");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String user : users) {
                writer.write(user + "\n");
            }
        } catch (IOException e) {
            throw new SystemException("Failed to delete user in FS");
        }
    }
}
