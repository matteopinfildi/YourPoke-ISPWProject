package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.cli.CliController;
import org.example.ispwproject.cli.CliHomePage;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.utils.bean.AddIngredientBean;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.enumeration.ingredient.ProteinAlternative;
import org.example.ispwproject.utils.exception.CliException;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CliProtein extends CliController {
    private int id;
    private PokeLabBean pokeLabBean;
    private BuyPokeLabAppController buyPokeLabAppController;
    private String total = "Total = 0.0 $";

    @Override
    public void init(int sID, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException, CliException {
        buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = sID;

        try{
            ProteinAlternative proteinAlternative = null;
            boolean flag = true;
            do{
                int selection = userSelection("Select a type of rice");
                switch (selection){
                    case 1:
                        proteinAlternative = ProteinAlternative.SALMON;
                        break;

                    case 2:
                        proteinAlternative = ProteinAlternative.SHRIMP;
                        break;

                    case 3:
                        proteinAlternative = ProteinAlternative.TUNA;
                        break;

                    case 4:
                        new CliHomePage().init(sID, pokeLabBean);
                        break;

                    default:
                        System.out.println("Select a valid option!");
                        flag = false;
                }
            }while (!flag);

            AddIngredientBean addIngredientBean = new AddIngredientBean("protein", proteinAlternative);
            buyPokeLabAppController.addIngredient(pokeLabBean, addIngredientBean);
        } catch (Exception e){
            throw new CliException("An error occurred while selecting the protein alternative", e);
        }

        if(pokeLabBean != null) {
            total = "Total = " + pokeLabBean.getPrice() + " $\n";
        }

        System.out.println(total);
        new CliPokeLab().init(sID, pokeLabBean);
    }

    @Override
    protected List<String> getAlternative() {
        return List.of(
                "Salmon: 4.00 $",
                "Shrimp: 3.00 $",
                "Tuna: 3.50 $"
        );
    }
}
