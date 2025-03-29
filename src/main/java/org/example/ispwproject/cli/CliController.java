package org.example.ispwproject.cli;

import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public abstract class CliController extends GraphicController {

    private static final Logger logger = Logger.getLogger(CliController.class.getName());

    public CliController(){}

    public abstract void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException;

    protected int menu(String title, List<String> alternative) {
        Scanner scanner = new Scanner(System.in);
        int selection = 0;

        logger.info(title);

        for (int i=0; i < alternative.size(); i++){
            logger.info(String.format("%d) %s%n", i+1, alternative.get(i)));
        }

        do{
            logger.info("");
            logger.info(String.format("Select an alternative (1-%d): ", alternative.size()));

            if (scanner.hasNextInt()) {
                selection = scanner.nextInt();
                if(selection < 1 || selection > alternative.size()){
                    logger.warning(String.format("Please, select a number between (1-%d)", alternative.size()));
                    selection = 0;
                }
            } else {
                logger.warning("Input not valid. Please, insert a number.");
                scanner.next();
            }
        } while (selection == 0);

        return selection;
    }

    protected abstract List<String> getAlternative();

    public int userSelection(String title) {
        List<String> alternative = getAlternative();
        return menu(title + "\n", alternative);
    }
}
