package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.cli.CliController;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.exception.CliException;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CliAddName extends CliController {
    private BuyPokeLabAppController buyPokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;

    @Override
    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException, CliException {
        this.buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = id;
        processPokeName();
    }


private void processPokeName() throws SystemException, IOException, LoginException, SQLException, CliException {
    Scanner scanner = new Scanner(System.in);
    boolean validName = false;

    while (!validName) {
        displayCurrentName();
        System.out.println("Enter name (or 'back' to return): ");
        String name = scanner.nextLine().trim();

        if (handleBackCommand(name)) return;

        if (updatePokeName(name)) {
            System.out.println(String.format("Poké name set to: %s", pokeLabBean.getPokeName()));
            validName = true;
            new CliPokeLab().init(id, pokeLabBean);
        }
    }
}


    private void displayCurrentName() {
        System.out.println("\n--- Set your Poké name ---");
        // Se getPokeName() restituisce null, assegna "no set"
        String currentName = (pokeLabBean.getPokeName() != null) ? pokeLabBean.getPokeName() : "no set";
        if (!currentName.equals("no set")) {
            System.out.println(String.format("Current name: %s", currentName));
        } else {
            System.out.println("No name has been set yet.");
        }
        System.out.println("(Name must be at least 4 characters long)");
    }

    private boolean handleBackCommand(String name) throws SystemException, IOException, LoginException, SQLException, CliException {
        if (name.equalsIgnoreCase("back")) {
            new CliPokeLab().init(id, pokeLabBean);
            return true;
        }
        return false;
    }



    private boolean updatePokeName(String name) {
        try {
            Session session = SessionManager.getSessionManager().getSessionFromId(id);
            SaveBean saveBean = new SaveBean(session.getUserId());
            return buyPokeLabAppController.setPokeName(pokeLabBean, name, saveBean);
        } catch (SystemException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }


    @Override
    protected List<String> getAlternative() {
        return List.of();
    }
}

