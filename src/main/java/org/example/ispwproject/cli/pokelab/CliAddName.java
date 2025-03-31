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
        buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = id;
        Scanner scanner = new Scanner(System.in);

        boolean validName = false;

        while (!validName) {
            logger.info("\n--- Set your Poké name ---");

            String currentName = pokeLabBean.getPokeName() != null ? pokeLabBean.getPokeName() : "Not set";
            logger.info(String.format("Current name: %s", currentName));

            logger.info("(Name must be at least 4 characters long)");
            logger.info("Enter name (or 'back' to return): ");
            String name = scanner.nextLine().trim();

            if (name.equalsIgnoreCase("back")) {
                new CliPokeLab().init(id, pokeLabBean);
                return; // Exit if 'back' is selected
            }

            // Validation for name length
            if (name.length() < 4) {
                logger.warning("Error: The name must be at least 4 characters long!");
            } else {
                // Proceed with saving the new name
                SessionManager sessionManager = SessionManager.getSessionManager();
                Session session = sessionManager.getSessionFromId(id);
                String userId = session.getUserId();

                // If the name is different from the current name, update it
                if (!name.equals(pokeLabBean.getPokeName())) {
                    pokeLabBean.setPokeName(name);
                    SaveBean saveBean = new SaveBean(userId);

                    // Attempt to save the new name
                    if (!buyPokeLabAppController.savePokeLab(pokeLabBean, saveBean)) {
                        logger.warning("Save failed!");
                        continue; // Only continue if save failed
                    }
                }

                // Successfully set the Poké name
                logger.info(String.format("Poké name set to: %s", name));
                validName = true; // Exit the loop once the name is valid

                // Proceed to the next screen
                new CliPokeLab().init(id, pokeLabBean);
            }
        }
    }

    @Override
    protected List<String> getAlternative() {return List.of();}
}
