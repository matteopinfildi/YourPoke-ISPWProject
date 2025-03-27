package org.example.ispwproject.model.observer;

import org.example.ispwproject.model.PokeWallObserver;
import org.example.ispwproject.model.pokewall.PokeWall;
import org.example.ispwproject.model.user.User;
import org.example.ispwproject.model.user.UserDAO;
import org.example.ispwproject.utils.dao.DAOFactory;

public class UserNotifier  implements PokeWallObserver {
    private UserDAO userDAO;

    public UserNotifier() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        this.userDAO = daoFactory.getUserDAO();
    }

    @Override
    public void update(PokeWall newPost) {
        // Qui implementi la logica per notificare gli utenti
        System.out.println("Notifica: Nuovo post da " + newPost.getUsername() +
                ": " + newPost.getPokeName());
    }

    private void sendNotification(User user, PokeWall post) {
        // da implementare l'invio effettivo della notifica
    }
}
