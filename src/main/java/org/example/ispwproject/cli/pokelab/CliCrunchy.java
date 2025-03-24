package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.cli.CliController;
import org.example.ispwproject.cli.CliHomePage;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.utils.bean.AddIngredientBean;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.enumeration.ingredient.CrunchyAlternative;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CliCrunchy extends CliController {
    private int id;
    private PokeLabBean pokeLabBean;
    private BuyPokeLabAppController buyPokeLabAppController;
    private String total = "Total = 0.0 $";

    @Override
    public void init(int sID, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = sID;

        try{
            CrunchyAlternative crunchyAlternative = null;
            boolean flag = true;
            do{
                int selection = userSelection("Select a type of rice");
                switch (selection){
                    case 1:
                        crunchyAlternative = CrunchyAlternative.ONION;
                        break;

                    case 2:
                        crunchyAlternative = CrunchyAlternative.NUTS;
                        break;

                    case 3:
                        crunchyAlternative = CrunchyAlternative.ALMONDS;
                        break;

                    case 4:
                        new CliHomePage().init(sID, pokeLabBean);
                        break;

                    default:
                        System.out.println("Select a valid option!");
                        flag = false;
                }
            }while (!flag);

            AddIngredientBean addIngredientBean = new AddIngredientBean("crunchy", crunchyAlternative);
            buyPokeLabAppController.addIngredient(pokeLabBean, addIngredientBean);
        } catch (Exception e){
            throw new RuntimeException(e);
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
                "Onion: 2.00 $",
                "Nuts: 1.00 $",
                "Almonds: 1.50 $"
        );
    }
}
