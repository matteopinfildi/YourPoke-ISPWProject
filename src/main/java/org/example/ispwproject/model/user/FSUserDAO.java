package org.example.ispwproject.model.user;

import org.example.ispwproject.model.pokelab.FSPokeLabDAO;
import org.example.ispwproject.model.pokelab.PokeLab;
import org.example.ispwproject.utils.enumeration.UserType;
import org.example.ispwproject.utils.exception.SystemException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FSUserDAO implements UserDAO {
    private static final String FILE_PATH = "user.csv";
    private static final String DELIMITER = ",";
    private static final Logger LOGGER = Logger.getLogger(FSUserDAO.class.getName());

    @Override
    public void create(User user) throws SystemException {
        if (userExists(user.getUsername())) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String line = String.join(DELIMITER,
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getuType().name(),
                    user.getAddress(),
                    "-1");
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new SystemException("Failed to create user: " + e.getMessage());
        }
    }

    private boolean userExists(String username) throws SystemException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            return reader.lines()
                    .anyMatch(line -> line.split(DELIMITER)[0].equals(username));
        } catch (IOException e) {
            throw new SystemException("Error checking user existence: " + e.getMessage());
        }
    }

    @Override
    public User read(String username) throws SystemException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                if (parts[0].equals(username)) {
                    return createUserFromParts(parts);
                }
            }
            return null;
        } catch (IOException e) {
            throw new SystemException("Error reading user: " + e.getMessage());
        }
    }

    private User createUserFromParts(String[] parts) throws SystemException {
        try {
            int plid = Integer.parseInt(parts[5]);
            PokeLab pokeLab = (plid != -1) ? new FSPokeLabDAO().read(plid) : null;

            User user = new User(
                    parts[0],
                    parts[1],
                    parts[2],
                    UserType.valueOf(parts[3]),
                    parts[4]
            );
            user.setPokeLab(pokeLab);
            return user;

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException _) {
            LOGGER.warning("Invalid user data format: " + String.join(DELIMITER, parts));
            throw new SystemException("Corrupted user data");
        }
    }

    @Override
    public void update(User user, int plid) throws SystemException {
        validatePokeLabExistence(plid);

        List<String> users = readAllUsers();
        boolean updated = false;

        for (int i = 0; i < users.size(); i++) {
            String[] parts = users.get(i).split(DELIMITER);
            if (parts[0].equals(user.getUsername())) {
                parts[5] = String.valueOf(plid);
                users.set(i, String.join(DELIMITER, parts));
                updated = true;
                break;
            }
        }

        if (updated) {
            writeAllUsers(users);
        }
    }

    private void validatePokeLabExistence(int plid) throws SystemException {
        if (plid != -1 && new FSPokeLabDAO().read(plid) == null) {
            throw new SystemException("PokeLab with ID " + plid + " does not exist");
        }
    }

    private List<String> readAllUsers() throws SystemException {
        List<String> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                users.add(line);
            }
            return users;
        } catch (IOException e) {
            throw new SystemException("Error reading users: " + e.getMessage());
        }
    }

    private void writeAllUsers(List<String> users) throws SystemException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String user : users) {
                writer.write(user);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new SystemException("Error updating users: " + e.getMessage());
        }
    }

    @Override
    public void delete(String username) throws SystemException {
        List<String> users = readAllUsers();
        boolean removed = users.removeIf(line -> line.split(DELIMITER)[0].equals(username));

        if (removed) {
            writeAllUsers(users);
        }
    }
}
