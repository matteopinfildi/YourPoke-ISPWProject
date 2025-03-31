package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.cli.CliController;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class CliAddName extends CliController {
    private static final Logger logger = Logger.getLogger(CliAddName.class.getName());
    private BuyPokeLabAppController buyPokeLabAppController;
    private PokeLabBean pokeLabBean;
    private int id;

    @Override
    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        this.buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = id;
        processPokeName();
    }

    private void processPokeName() throws SystemException, IOException, LoginException, SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean validName = false;

        while (!validName) {
            displayCurrentName();
            logger.info("Enter name (or 'back' to return): ");
            String name = scanner.nextLine().trim();

            if (handleBackCommand(name)) return;
            if (!isValidName(name)) continue;

            if (updatePokeName(name)) {
                logger.info(String.format("Poké name set to: %s", name));
            }
            validName = true;
            new CliPokeLab().init(id, pokeLabBean);
        }
    }

    private void displayCurrentName() {
        logger.info("\n--- Set your Poké name ---");
        String currentName = pokeLabBean.getPokeName();
        logger.info(currentName != null ? "Current name: " + currentName : "Current name: no set");
        logger.info("(Name must be at least 4 characters long)");
    }

    private boolean handleBackCommand(String name) throws SystemException, IOException, LoginException, SQLException {
        if (name.equalsIgnoreCase("back")) {
            new CliPokeLab().init(id, pokeLabBean);
            return true;
        }
        return false;
    }

    private boolean isValidName(String name) {
        if (name.length() < 4) {
            logger.warning("Error: The name must be at least 4 characters long!");
            return false;
        }
        return true;
    }

    private boolean updatePokeName(String name) {
        if (!name.equals(pokeLabBean.getPokeName())) {
            Session session = SessionManager.getSessionManager().getSessionFromId(id);
            SaveBean saveBean = new SaveBean(session.getUserId());
            pokeLabBean.setPokeName(name);

            if (!buyPokeLabAppController.savePokeLab(pokeLabBean, saveBean)) {
                logger.warning("Save failed!");
                return false;
            }
        }
        return true;
    }

    @Override
    protected List<String> getAlternative() {
        return List.of();
    }
}

