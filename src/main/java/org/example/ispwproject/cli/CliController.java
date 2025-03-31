package org.example.ispwproject.cli;

import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class CliController extends GraphicController {
    private static final Logger logger = Logger.getLogger(CliController.class.getName());

    protected CliController() {}

    public abstract void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException;

    protected int menu(String title, List<String> alternative) {
        Scanner scanner = new Scanner(System.in);
        logMenuOptions(title, alternative);
        return getUserSelection(scanner, alternative.size());
    }

    private void logMenuOptions(String title, List<String> alternative) {
        if (!logger.isLoggable(Level.INFO)) return;

        logger.info(title);
        for (int i = 0; i < alternative.size(); i++) {
            logger.info((i + 1) + ") " + alternative.get(i));
        }
    }

    private int getUserSelection(Scanner scanner, int maxOptions) {
        int selection;
        do {
            logger.info(String.format("Select an alternative (1-" + maxOptions + "): ", maxOptions));
            selection = readUserInput(scanner, maxOptions);
        } while (selection == 0);
        return selection;
    }

    private int readUserInput(Scanner scanner, int maxOptions) {
        if (!scanner.hasNextInt()) {
            logWarning("Input not valid. Please, insert a number.");
            scanner.next();
            return 0;
        }
        int selection = scanner.nextInt();
        if (selection < 1 || selection > maxOptions) {
            logWarning(String.format("Please, select a number between (1-" + maxOptions + ")", maxOptions));
            return 0;
        }
        return selection;
    }

    private void logWarning(String message) {
        if (logger.isLoggable(Level.WARNING)) {
            logger.warning(message);
        }
    }

    protected abstract List<String> getAlternative();

    public int userSelection(String title) {
        return menu(title + "\n", getAlternative());
    }
}

