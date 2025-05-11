package org.example.ispwproject.model.user;

import org.example.ispwproject.model.pokelab.DBPokeLabDAO;
import org.example.ispwproject.model.pokelab.PokeLab;
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
        boolean exists = false;

        // Primo blocco: controllo esistenza
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(DELIMITER);
                if (data[0].equals(userA.getUsername())) {
                    exists = true;
                    break;
                }
            }
        } catch (FileNotFoundException _) {
            // Il file non esiste, verrà creato nel blocco di scrittura
        } catch (IOException _) {
            throw new SystemException("Failed to check user in FS");
        }

        if (exists) return;

        // Secondo blocco: scrittura
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bufferedWriter.write(userA.getUsername() + DELIMITER +
                    userA.getPassword() + DELIMITER +
                    userA.getEmail() + DELIMITER +
                    userA.getuType().name() + DELIMITER +
                    userA.getAddress() + DELIMITER +
                    "-1" + "\n");
        } catch (IOException _) {
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
        } catch (IOException _) {
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
        } catch (IOException _) {
            throw new SystemException("Failed to read users in FS");
        }

        if (!updated) return; // L'utente non esiste, nessun aggiornamento

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String user : users) {
                bufferedWriter.write(user + "\n");
            }
        } catch (IOException _) {
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
        } catch (IOException _) {
            throw new SystemException("Failed to read user in FS");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String user : users) {
                writer.write(user + "\n");
            }
        } catch (IOException _) {
            throw new SystemException("Failed to delete user in FS");
        }
    }
}
