package org.example.ispwproject.cli;

import org.example.ispwproject.control.graphic.GraphicController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.CliException;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public abstract class CliController extends GraphicController {

    protected    CliController() {}

    public abstract void init(int id, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException, CliException;

    protected int menu(String title, List<String> alternative) {
        Scanner scanner = new Scanner(System.in);
        int selection = 0;

        System.out.printf(title);
        for (int i = 0; i < alternative.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, alternative.get(i));
        }


        do {
            System.out.println();
            System.out.printf("Select an alternative (1-%d): ", alternative.size());

            if (scanner.hasNextInt()) {
                selection = scanner.nextInt();
                if (selection < 1 || selection > alternative.size()) {
                    System.out.printf("Please, select a number between (1-%d)%n", alternative.size());
                    selection = 0;
                }
            } else {
                System.out.println("Input not valid. Please, insert a number.");
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
