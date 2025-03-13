package org.example.ispwproject.model.user;

import org.example.ispwproject.model.decorator.InMemoryPokeLabDAO;
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
    public User read(String Uid) throws SystemException {
        for (User user : listOfUsers) {
            if (user.getUsername().equals(Uid)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void update(User user, int plid) throws SystemException {
        delete(user.getUsername());
        user.setPokeLab(InMemoryPokeLabDAO.getInstance().read(plid));
        listOfUsers.add(user);
    }

    @Override
    public void delete(String Uid) throws SystemException {
        try {
            listOfUsers.remove(read(Uid));
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }
}
