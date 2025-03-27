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

public class CliAddName extends CliController {
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

        do{
            System.out.println("\n--- Set your Poké name ---");
            System.out.println("Current name: " +
                    (pokeLabBean.getPokeName() != null ? pokeLabBean.getPokeName() : "Not set"));
            System.out.println("(Name must be at least 4 characters long)");
            System.out.print("Enter name (or 'back' to return): ");

            String name = scanner.nextLine().trim();

            if (name.equalsIgnoreCase("back")){
                new CliPokeLab().init(id, pokeLabBean);
                return;
            }

            if (name.length() < 4) {
                System.out.println("Error: The name must be at least 4 characters long!");
                continue;
            }

            SessionManager sessionManager = SessionManager.getSessionManager();
            Session session = sessionManager.getSessionFromId(id);
            String userId = session.getUserId();

            // Se il nome è lo stesso di prima, non serve salvare di nuovo
            if (!name.equals(pokeLabBean.getPokeName())) {
                pokeLabBean.setPokeName(name);
                SaveBean saveBean = new SaveBean(userId);

                if (!buyPokeLabAppController.savePokeLab(pokeLabBean, saveBean)) {
                    System.out.println("Save failed!");
                    continue;
                }
            }

            System.out.println("Poké name set to: " + name);
            validName = true;

            new CliPokeLab().init(id, pokeLabBean);
        }while(!validName);
    }

    @Override
    protected List<String> getAlternative() {return List.of();}
}
