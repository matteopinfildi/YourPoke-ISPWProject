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
        // viene verificato se esiste già uno user con lo stesso username per evitare duplicati
        if (userExists(user.getUsername())) {
            return;
        }

        // si apre il file in append
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // vengono scritte tutte le info dello user sul file
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

    // metodo che verifica se esiste già uno user con lo stesso username per evitare duplicati
    private boolean userExists(String username) throws SystemException {
        // legge sul file e ottiene uno stream<String> di tutte le righe del file
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
            // scorriamo su tutte e righe del file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER); // divide la riga letta in un array di stringhe parts, usando DELIMITER come separatore
                if (parts[0].equals(username)) {
                    // se il primo valore del file e lo username combaciano viene richiamato il metodo
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
            int plid = Integer.parseInt(parts[5]); // assegna alla variabile plid l'effettivo id del poke lab associato allo user, che viene convertito in int
            // viene chiamato read sul fs del poke lab per recuperarlo
            PokeLab pokeLab = (plid != -1) ? new FSPokeLabDAO().read(plid) : null;

            // viene creato un nuovo oggetto user con le rispettive parti
            User user = new User(
                    parts[0],
                    parts[1],
                    parts[2],
                    UserType.valueOf(parts[3]), // converte la stringa parts[3] in un valore dell’enum UserType
                    parts[4]
            );
            user.setPokeLab(pokeLab); // viene impostato nello user appena creato, il poke lab recuperato
            return user;

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException _) {
            LOGGER.warning("Invalid user data format: " + String.join(DELIMITER, parts));
            throw new SystemException("Corrupted user data");
        }
    }

    @Override
    public void update(User user, int plid) throws SystemException {
        // invoco il metodo che verifica se il poke lab con id pari a plid esista davvero
        validatePokeLabExistence(plid);

        // invoco il metodo che legge tutte le righe del file e le restituisce come una lista di stringhe
        List<String> users = readAllUsers();
        boolean updated = false;

        // ciclo for su tutti gli indici della lista users
        for (int i = 0; i < users.size(); i++) {
            String[] parts = users.get(i).split(DELIMITER);
            if (parts[0].equals(user.getUsername())) {
                // se gli username combaciano, sostituisco il valore del plid vecchio con il valore nuovo, così da aggiornarlo
                parts[5] = String.valueOf(plid);
                users.set(i, String.join(DELIMITER, parts)); // modifico la lista di stringhe delle righe del fie
                updated = true;
                break;
            }
        }

        if (updated) {
            // se l'update è stato eseguito viene invocato il metodo che sovrascrive il file con tutte le righe contenute nella lista aggiornata users
            writeAllUsers(users);
        }
    }

    // metodo che verifica se il poke lab con id pari a plid esista davvero
    private void validatePokeLabExistence(int plid) throws SystemException {
        if (plid != -1 && new FSPokeLabDAO().read(plid) == null) {
            throw new SystemException("PokeLab with ID " + plid + " does not exist");
        }
    }

    // metodo che legge tutte le righe del file e le restituisce come una lista di stringhe
    private List<String> readAllUsers() throws SystemException {
        List<String> users = new ArrayList<>(); // lista vuota di stringhe in cui verranno memorizzate tutte le righe lette
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            // si scorrono tutte le righe del file
            while ((line = reader.readLine()) != null) {
                // aggiunge la riga alla lista
                users.add(line);
            }
            // alla fine del ciclo ritorna la lista di tutte le righe lette
            return users;
        } catch (IOException e) {
            throw new SystemException("Error reading users: " + e.getMessage());
        }
    }

    // metodo che sovrascrive il file con tutte le righe contenute nella lista aggiornata users (effettua l'update effettivo su file, quindi lo riscrive)
    private void writeAllUsers(List<String> users) throws SystemException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // cicliamo su tutta la lista di righe lette
            for (String user : users) {
                // scrive ogni riga sul file (scriverà anche quella aggiornata)
                writer.write(user);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new SystemException("Error updating users: " + e.getMessage());
        }
    }

    @Override
    public void delete(String username) throws SystemException {
        // invoco il metodo che legge tutte le righe del file e le restituisce come una lista di stringhe
        List<String> users = readAllUsers();
        // confronta gli username e se sono uguali pone il flag removed = true
        boolean removed = users.removeIf(line -> line.split(DELIMITER)[0].equals(username));

        if (removed) {
            // se la delete è stata eseguita viene invocato il metodo che sovrascrive il file con tutte le righe contenute nella lista aggiornata users
            writeAllUsers(users);
        }
    }
}
