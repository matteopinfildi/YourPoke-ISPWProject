package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.cli.CliController;
import org.example.ispwproject.cli.CliHomePage;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.utils.bean.AddIngredientBean;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.enumeration.ingredient.SaucesAlternative;
import org.example.ispwproject.utils.exception.CliException;
import org.example.ispwproject.utils.exception.PokeLabSystemException;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CliSauces extends CliController {
    private int id;
    private PokeLabBean pokeLabBean;
    private BuyPokeLabAppController buyPokeLabAppController;
    private String total = "Total = 0.0 $";

    @Override
    public void init(int sID, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException, CliException, PokeLabSystemException {
        buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = sID;

        try{
            SaucesAlternative saucesAlternative = null;
            boolean flag = true;
            do{
                int selection = userSelection("Select a type of rice");
                switch (selection){
                    case 1:
                        saucesAlternative = SaucesAlternative.TERIYAKI;
                        break;

                    case 2:
                        saucesAlternative = SaucesAlternative.SOY;
                        break;

                    case 3:
                        saucesAlternative = SaucesAlternative.WASABI;
                        break;

                    case 4:
                        new CliHomePage().init(sID, pokeLabBean);
                        break;

                    default:
                        System.out.println("Select a valid option!");
                        flag = false;
                }
            }while (!flag);

            AddIngredientBean addIngredientBean = new AddIngredientBean("sauces", saucesAlternative);
            buyPokeLabAppController.addIngredient(pokeLabBean, addIngredientBean);
        } catch (Exception e){
            throw new CliException("An error occurred while selecting the sauce alternative", e);
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
                "Teriyaki: 2.00 $",
                "Soy: 1.00 $",
                "Wasabi: 1.50 $"
        );
    }
}
