package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.cli.CliController;
import org.example.ispwproject.cli.CliHomePage;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.utils.bean.AddIngredientBean;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.enumeration.ingredient.*;
import org.example.ispwproject.utils.exception.CliException;
import org.example.ispwproject.utils.exception.SystemException;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public abstract class CliIngredient<T extends Enum<T>> extends CliController {
    private int id;
    private PokeLabBean pokeLabBean;
    private BuyPokeLabAppController buyPokeLabAppController;
    private String total = "Total = 0.0 $";
    private String calories = "Calories = 0 cal";
    private final String ingredientType;
    private final List<T> alternatives;
    private final List<String> alternativeDescriptions;

    protected CliIngredient(String ingredientType, List<T> alternatives, List<String> alternativeDescriptions) {
        this.ingredientType = ingredientType;
        this.alternatives = alternatives;
        this.alternativeDescriptions = alternativeDescriptions;
    }

    @Override
    public void init(int sID, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException, CliException {
        buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = sID;
        try {
            T selection = getUserSelection();
            if (selection != null) {
                AddIngredientBean addIngredientBean = new AddIngredientBean(ingredientType, (GenericOption) selection);
                buyPokeLabAppController.addIngredient(pokeLabBean, addIngredientBean);
            }
        } catch (Exception e) {
            throw new CliException("An error occurred while selecting " + ingredientType, e);
        }
        updateTotal();
        updateCalories();
        new CliPokeLab().init(sID, pokeLabBean);
    }

    private T getUserSelection() throws SystemException, CliException, SQLException, LoginException, IOException {
        boolean flag;
        T selectedAlternative = null;
        do {
            flag = true;
            int selection = userSelection("Select a type of " + ingredientType);
            if (selection >= 1 && selection <= alternatives.size()) {
                selectedAlternative = alternatives.get(selection - 1);
            } else if (selection == alternatives.size() + 1) {
                new CliHomePage().init(id, pokeLabBean);
                return null;
            } else {
                System.out.println("Select a valid option!");
                flag = false;
            }
        } while (!flag);
        return selectedAlternative;
    }

    private void updateTotal() {
        if (pokeLabBean != null) {
            total = "Total = " + pokeLabBean.getPrice() + " $";
        }
        System.out.println(total);
    }

    private void updateCalories(){
        if (pokeLabBean!= null){
            calories = "Calories = " + pokeLabBean.getCalories() + " cal\n";
        }
        System.out.println(calories);
    }

    @Override
    protected List<String> getAlternative() {
        return alternativeDescriptions;
    }
}