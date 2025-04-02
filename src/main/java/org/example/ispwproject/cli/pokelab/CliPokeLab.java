package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.cli.CliController;
import org.example.ispwproject.cli.CliHomePage;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.enumeration.ingredient.*;
import org.example.ispwproject.utils.exception.CliException;
import org.example.ispwproject.utils.exception.PokeLabSystemException;
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
    private static final String NO_SELECTION = "No selection";

    String riceName;
    String proteinName;
    String fruitName;
    String crunchyName;
    String saucesName;
    String bowlSize;
    String pokeName;

    public static void setRecover (boolean value) {recover =  value;}

    private void popup() throws PokeLabSystemException {
        Scanner scanner = new Scanner(System.in);
        int selection = -1;
        boolean validInput = false;
        do {
            System.out.println("1) Recover Pokè Lab");
            System.out.println("2) New Pokè Lab");
            System.out.println("\nSelect an option: ");

            validInput = scanner.hasNextInt();
            if (!validInput) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consuma l'input non valido
                continue;
            }

            selection = scanner.nextInt();
            switch (selection) {
                case 1 -> recoverPokeLab();
                case 2 -> System.out.println("Creating a new Pokè Lab...");
                default -> {
                    System.out.println("Invalid option. Please select 1 or 2.");
                    validInput = false;
                }
            }

        }while (validInput) ;
    }

    private void recoverPokeLab() throws PokeLabSystemException {
        SessionManager sessionManager = SessionManager.getSessionManager();
        Session session = sessionManager.getSessionFromId(id);
        String userId = session.getUserId();
        SaveBean saveBean = new SaveBean(userId);
        PokeLabBean oldPokeLabBean = buyPokeLabAppController.recoverPokeLab(saveBean);

        if (oldPokeLabBean != null) {
            this.pokeLabBean = oldPokeLabBean;
            System.out.println("Pokè Lab successfully recovered.");
        } else {
            System.out.println("Pokè Lab not found!");
        }
    }


    @Override
    public void init(int sID, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException, CliException, PokeLabSystemException, org.example.ispwproject.utils.exception.LoginException {
        buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = sID;

        // Recupero se necessario
        if (recover) {
            popup();
            setRecover(false);
        }

        // Aggiorna il totale se esiste un pokeLabBean
        if (pokeLabBean != null) {
            total = "Total = " + pokeLabBean.getPrice() + " $";
        }

        // Seleziona gli ingredienti
        setIngredients();

        // Gestione della selezione dell'utente per gli ingredienti
        boolean condition;
        do {
            condition = handleUserSelection();
        } while (!condition);
    }

    // Metodo per impostare gli ingredienti
    private void setIngredients() {
        riceName = getIngredientName("rice");
        proteinName = getIngredientName("protein");
        fruitName = getIngredientName("fruit");
        crunchyName = getIngredientName("crunchy");
        saucesName = getIngredientName("sauces");
        bowlSize = (pokeLabBean.getBowlSize() != null) ? pokeLabBean.getBowlSize() : NO_SELECTION;
        pokeName = (pokeLabBean.getPokeName() != null) ? pokeLabBean.getPokeName() : "No name set";
    }

    private String getIngredientName(String ingredientType) {
        GenericAlternative ingredient = pokeLabBean.getIngredient(ingredientType);
        return (ingredient != null) ? ingredient.toString() : NO_SELECTION;
    }
    // Metodo per gestire la selezione dell'utente
    private boolean handleUserSelection() throws SystemException, CliException, SQLException, LoginException, IOException, org.example.ispwproject.utils.exception.LoginException, PokeLabSystemException {
        int selection = userSelection("Select an ingredient for your Poke Lab");

        switch (selection) {
            case 1:
                new CliRice().init(id, pokeLabBean);
                return true;
            case 2:
                new CliProtein().init(id, pokeLabBean);
                return true;
            case 3:
                new CliFruit().init(id, pokeLabBean);
                return true;
            case 4:
                new CliCrunchy().init(id, pokeLabBean);
                return true;
            case 5:
                new CliSauces().init(id, pokeLabBean);
                return true;
            case 6:
                new CliBowlSize().init(id, pokeLabBean);
                return true;
            case 7:
                new CliAddName().init(id, pokeLabBean);
                return true;
            case 8:
                return savePokeLab(); // Gestisci il salvataggio
            case 9:
                new CliHomePage().init(id, pokeLabBean);
                return true;
            default:
                System.out.println("Select a valid option!");
                return false;
        }
    }

    // Metodo per salvare il Pokè Lab
    private boolean savePokeLab() throws PokeLabSystemException {
        SessionManager sessionManager = SessionManager.getSessionManager();
        Session session = sessionManager.getSessionFromId(id);
        String userId = session.getUserId();
        SaveBean saveBean = new SaveBean(userId);

        if (!buyPokeLabAppController.savePokeLab(pokeLabBean, saveBean)) {
            System.out.println("Save failed\n");
            return false;
        } else {
            System.out.println("Save successful\n");
            return true;
        }
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
                "Save Pokè Lab",
                "Back"
        );
    }
}
