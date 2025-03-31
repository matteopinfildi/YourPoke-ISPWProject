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
    public void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException{
        buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = id;

        Scanner scanner = new Scanner(System.in);
        boolean validName= false;

        while (!validName) {
            logger.info("\n--- Set your Poké name ---");
            logger.info("Current name: " +
                    (pokeLabBean.getPokeName() != null ? pokeLabBean.getPokeName() : "Not set"));
            logger.info("(Name must be at least 4 characters long)");
            logger.info("Enter name (or 'back' to return): ");
            String name = scanner.nextLine().trim();

            if (name.equalsIgnoreCase("back")) {
                new CliPokeLab().init(id, pokeLabBean);
                return;
            }

            if (name.length() < 4) {
                logger.warning("Error: The name must be at least 4 characters long!");
                continue; // Continue if name is too short
            }

            SessionManager sessionManager = SessionManager.getSessionManager();
            Session session = sessionManager.getSessionFromId(id);
            String userId = session.getUserId();

            // If the name is the same as before, no need to save it again
            if (!name.equals(pokeLabBean.getPokeName())) {
                pokeLabBean.setPokeName(name);
                SaveBean saveBean = new SaveBean(userId);
                if (!buyPokeLabAppController.savePokeLab(pokeLabBean, saveBean)) {
                    logger.warning("Save failed!");
                    continue; // Continue if saving fails
                }
            }

            logger.info("Poké name set to: " + name);
            validName = true; // Exit the loop if the name is valid
            new CliPokeLab().init(id, pokeLabBean);
        }
    }

    @Override
    protected List<String> getAlternative() {return List.of();}
}
