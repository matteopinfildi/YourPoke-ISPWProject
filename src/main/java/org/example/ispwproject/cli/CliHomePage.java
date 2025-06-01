package org.example.ispwproject.cli;

import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.cli.pokelab.CliPokeLab;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.exception.CliException;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class CliHomePage extends CliController{

    public CliHomePage(){
        // costruttore vuoto
    }

    private int sID;
    private PokeLabBean pokeLabBean;
    private BuyPokeLabAppController buyPokeLabAppController;

    @Override
    public void init(int sID, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException, CliException {
        this.sID = sID;
        this.pokeLabBean = pokeLabBean;

        boolean condition = true;
        do{
            int selection = userSelection("HomePage");
            switch (selection){
                case 1:
                    new CliLogin().login(sID, pokeLabBean);
                    break;

                case 2:
                    System.out.println("Buy Classic Pokè");
                    break;

                case 3:
                    SessionManager sessionManager = SessionManager.getSessionManager();
                    Session session = sessionManager.getSessionFromId(sID);

                    // Controllo se l'utente è loggato
                    if (session == null) {
                        System.out.println("You need to log in first!");
                        break;
                    }


                    buyPokeLabAppController = new BuyPokeLabAppController();
                    pokeLabBean = buyPokeLabAppController.newPokeLab();
                    checkRetrieve();
                    new CliPokeLab().init(sID, pokeLabBean);
                    break;

                case 4:
                    System.out.println("Pokè Wall");
                    break;

                case 5:
                    exit();
                    condition = false;
                    break;
                default:
                    System.out.println("Select a valid option!");
            }
        }while(condition);
    }

    public void checkRetrieve() throws SystemException {
        SessionManager sessionManager = SessionManager.getSessionManager();
        Session session = sessionManager.getSessionFromId(sID);

        SaveBean saveBean = new SaveBean(session.getUserId());
        boolean v = buyPokeLabAppController.checkPokeLab(saveBean);
        CliPokeLab.setRetrieve(v);
    }

    @Override
    protected List<String> getAlternative(){
        return List.of("Login", "Buy Classic Pokè", "Buy Pokè Lab", "Pokè Wall", "Exit");
    }

    private void exit() {
        System.out.println("Exit");
        System.exit(0);
    }
}