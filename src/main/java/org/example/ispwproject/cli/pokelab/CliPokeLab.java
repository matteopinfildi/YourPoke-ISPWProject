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

public class CliPokeLab extends CliController{

    private int id;
    private PokeLabBean pokeLabBean;
    private BuyPokeLabAppController buyPokeLabAppController;
    private String total = "Total = 0.0 $";
    private static boolean recover = false;

    String riceName;
    String proteinName;
    String fruitName;
    String crunchyName;
    String saucesName;


    public static void setRecover (boolean value) {recover =  value;}

    private void popup() {
        Scanner scanner = new Scanner(System.in);
        int selection = -1;
        boolean flag = false;
        do{
            System.out.println("1) Recover Pokè Lab");
            System.out.println("2) New Pokè Lab");
            System.out.println("\nSelect an option: ");
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
                    } else {System.out.println("Pokè Lab not found!");}
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
        riceName = (genericAlternative != null) ? ((RiceAlternative) genericAlternative).name() : "No selection";

        genericAlternative = pokeLabBean.getIngredient("protein");
        proteinName = (genericAlternative != null) ? ((ProteinAlternative) genericAlternative).name() : "No selection";

        genericAlternative = pokeLabBean.getIngredient("fruit");
        fruitName = (genericAlternative != null) ? ((FruitAlternative) genericAlternative).name() : "No selection";

        genericAlternative = pokeLabBean.getIngredient("crunchy");
        crunchyName = (genericAlternative != null) ? ((CrunchyAlternative) genericAlternative).name() : "No selection";

        genericAlternative = pokeLabBean.getIngredient("sauces");
        saucesName = (genericAlternative != null) ? ((SaucesAlternative) genericAlternative).name() : "No selection";

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
                    new CliHomePage().init(sID, pokeLabBean);
                    break;

                case 7:
                    SessionManager sessionManager = SessionManager.getSessionManager();
                    Session session = sessionManager.getSessionFromId(id);
                    String userId = session.getUserId();

                    SaveBean saveBean = new SaveBean(userId);

                    if (!buyPokeLabAppController.savePokeLab(pokeLabBean, saveBean)) {
                        System.out.println("Save failed\n");
                        condition = false;
                        break;
                    } else {
                        System.out.println("Save successfull\n");
                        // mettere il ristorante
                    }
                    break;

                default:
                    System.out.println("Select a valid option!");
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
                "Back",
                "Add Name"
        );
    }
}
