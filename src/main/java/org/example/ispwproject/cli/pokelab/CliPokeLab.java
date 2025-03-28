package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.cli.CliController;
import org.example.ispwproject.cli.CliHomePage;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.enumeration.ingredient.*;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class CliPokeLab extends CliController{

    private static final Logger logger = Logger.getLogger(CliPokeLab.class.getName());
    private int id;
    private PokeLabBean pokeLabBean;
    private BuyPokeLabAppController buyPokeLabAppController;
    private String total = "Total = 0.0 $";
    private static boolean recover = false;
    private static final String NO_SELECTION = "No selection";

    String riceName;
    String proteinName;
    String fruitName;
    String crunchyName;
    String saucesName;
    String bowlSize;
    String pokeName;

    public static void setRecover (boolean value) {recover =  value;}

    private void popup() {
        Scanner scanner = new Scanner(System.in);
        int selection = -1;
        boolean flag = false;
        do{
            logger.info("1) Recover Pokè Lab");
            logger.info("2) New Pokè Lab");
            logger.info("\nSelect an option: ");
            if(scanner.hasNextInt()) {
                selection = scanner.nextInt();
                if(selection == 1){
                    SessionManager sessionManager = SessionManager.getSessionManager();
                    Session session = sessionManager.getSessionFromId(id);
                    String userId = session.getUserId();

                    SaveBean saveBean = new SaveBean(userId);

                    PokeLabBean oldPokeLabBean = buyPokeLabAppController.recoverPokeLab(saveBean);
                    if (oldPokeLabBean != null) {
                        this.pokeLabBean = oldPokeLabBean;
                    } else {logger.info("Pokè Lab not found!");}
                } else if (selection == 2) {

                } else {
                    flag = true;
                }
            }
        }while (flag);
    }


    @Override
    public void init(int sID, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = sID;

        if (recover) {
            popup();
            boolean flag = false;
            setRecover(flag);
        }

        if(pokeLabBean != null) {
            total = "Total = " + pokeLabBean.getPrice() + " $";
        }

        GenericAlternative genericAlternative;

        genericAlternative = pokeLabBean.getIngredient("rice");
        riceName = (genericAlternative != null) ? ((RiceAlternative) genericAlternative).name() : NO_SELECTION;

        genericAlternative = pokeLabBean.getIngredient("protein");
        proteinName = (genericAlternative != null) ? ((ProteinAlternative) genericAlternative).name() : NO_SELECTION;

        genericAlternative = pokeLabBean.getIngredient("fruit");
        fruitName = (genericAlternative != null) ? ((FruitAlternative) genericAlternative).name() : NO_SELECTION;

        genericAlternative = pokeLabBean.getIngredient("crunchy");
        crunchyName = (genericAlternative != null) ? ((CrunchyAlternative) genericAlternative).name() : NO_SELECTION;

        genericAlternative = pokeLabBean.getIngredient("sauces");
        saucesName = (genericAlternative != null) ? ((SaucesAlternative) genericAlternative).name() : NO_SELECTION;

        bowlSize = pokeLabBean.getBowlSize() != null ? pokeLabBean.getBowlSize() : NO_SELECTION;

        pokeName = pokeLabBean.getPokeName() != null ? pokeLabBean.getPokeName() : "No name set";

        boolean condition;

        do{
            condition = true;
            int selection = userSelection("Select an ingredient for your Poke Lab");
            switch (selection) {
                case 1:
                    new CliRice().init(sID, pokeLabBean);
                    break;
                case 2:
                    new CliProtein().init(sID, pokeLabBean);
                    break;
                case 3:
                    new CliFruit().init(sID, pokeLabBean);
                    break;
                case 4:
                    new CliCrunchy().init(sID, pokeLabBean);
                    break;
                case 5:
                    new CliSauces().init(sID, pokeLabBean);
                    break;

                case 6:
                    new CliBowlSize().init(sID, pokeLabBean);
                    break;

                case 7:
                    new CliAddName().init(sID, pokeLabBean);
                    break;
                case 8:
                    new CliHomePage().init(sID, pokeLabBean);
                    break;

                case 9:
                    SessionManager sessionManager = SessionManager.getSessionManager();
                    Session session = sessionManager.getSessionFromId(id);
                    String userId = session.getUserId();

                    SaveBean saveBean = new SaveBean(userId);

                    if (!buyPokeLabAppController.savePokeLab(pokeLabBean, saveBean)) {
                        logger.info("Save failed\n");
                        condition = false;
                        break;
                    } else {
                        logger.info("Save successfull\n");
                        // mettere add name
                    }
                    break;

                default:
                    logger.info("Select a valid option!");
                    condition = false;
            }
        }while(!condition);
    }

    @Override
    protected List<String> getAlternative() {
        return List.of(
                "Rice: " + riceName,
                "Protein: " + proteinName,
                "Fruit: " + fruitName,
                "Crunchy: " + crunchyName,
                "Sauces: " + saucesName,
                "Bowl size: " + bowlSize,
                "Add Name: " + pokeName,
                "Back",
                "Save Pokè Lab"
        );
    }
}
