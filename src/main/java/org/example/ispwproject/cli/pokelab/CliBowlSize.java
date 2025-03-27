package org.example.ispwproject.cli.pokelab;

import org.example.ispwproject.cli.CliController;
import org.example.ispwproject.cli.CliHomePage;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CliBowlSize extends CliController {
    private int id;
    private PokeLabBean pokeLabBean;
    private BuyPokeLabAppController buyPokeLabAppController;

    @Override
    public void init(int sID, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException {
        buyPokeLabAppController = new BuyPokeLabAppController();
        this.pokeLabBean = pokeLabBean;
        this.id = sID;

        try{
            String selectedSize = null;
            boolean flag = true;

            do{
                int selection = userSelection("Select a size for your bowl: ");
                switch (selection) {
                    case 1:
                        selectedSize = "Small";
                        break;
                    case 2:
                        selectedSize = "Medium";
                        break;
                    case 3:
                        selectedSize = "Large";
                        break;
                    case 4:
                        new CliHomePage().init(sID, pokeLabBean);
                        return;
                    default:
                        System.out.println("Select a valid option!");
                        flag = false;
                }

            }while (!flag);

            if (!selectedSize.equals(pokeLabBean.getBowlSize())) {
                pokeLabBean.setBowlSize(selectedSize);

                SaveBean saveBean = new SaveBean(String.valueOf(pokeLabBean.getId()));
                if (!buyPokeLabAppController.savePokeLab(pokeLabBean, saveBean)) {
                    System.out.println("Error saving bowl size");
                } else {
                    System.out.println("Bowl size saved successfully: " + selectedSize);
                }
            }

            new CliPokeLab().init(sID, pokeLabBean);

        } catch (Exception e){
            throw new RuntimeException(e);
        }


    }

    @Override
    protected List<String> getAlternative() {
        return List.of(
                "Small: 250 grams",
                "Medium: 400 grams",
                "Large: 600 grams"
        );
    }
}
