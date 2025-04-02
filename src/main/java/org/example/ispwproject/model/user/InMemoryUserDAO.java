package org.example.ispwproject.model.user;

import org.example.ispwproject.model.decorator.pokelab.InMemoryPokeLabDAO;
import org.example.ispwproject.utils.exception.PokeLabSystemException;
import org.example.ispwproject.utils.exception.SystemException;

import java.util.ArrayList;
import java.util.Collection;

public class InMemoryUserDAO implements UserDAO{
    private static InMemoryUserDAO instance;

    private InMemoryUserDAO(){}

    public static InMemoryUserDAO getInstance() {
        if (instance == null) {
            instance = new InMemoryUserDAO();
        }
        return instance;
    }

    private Collection<User> listOfUsers = new ArrayList<>();

    @Override
    public void create(User userA) throws SystemException {
        listOfUsers.add(userA);
    }

    @Override
    public User read(String uId) throws SystemException {
        for (User user : listOfUsers) {
            if (user.getUsername().equals(uId)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void update(User user, int plid) throws SystemException, PokeLabSystemException {
        if (user == null) {
            throw new PokeLabSystemException("Errore: l'utente passato al metodo update() è nullo.");
        }
        // Controlla che l'utente esista nel sistema
        if (read(user.getUsername()) == null) {
            throw new PokeLabSystemException("Errore: utente non trovato per l'aggiornamento.");
        }

        // Procedi con l'aggiornamento
        delete(user.getUsername());
        user.setPokeLab(InMemoryPokeLabDAO.getInstance().read(plid));
        listOfUsers.add(user);
    }

    @Override
    public void delete(String uId) throws SystemException, PokeLabSystemException {
        try {
            listOfUsers.remove(read(uId));
        } catch (SystemException e) {
            throw new PokeLabSystemException("Error creating user", e);
        }
    }
}
