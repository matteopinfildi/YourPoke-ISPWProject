package org.example.ispwproject.control.application;

import org.example.ispwproject.model.Ingredient;
import org.example.ispwproject.model.decorator.PokeLab;
import org.example.ispwproject.model.decorator.PokeLabDAO;
import org.example.ispwproject.model.user.User;
import org.example.ispwproject.model.user.UserDAO;
import org.example.ispwproject.utils.bean.AddIngredientBean;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.dao.DAOFactory;
import org.example.ispwproject.utils.enumeration.ingredient.GenericAlternative;
import org.example.ispwproject.utils.exception.SystemException;

public class BuyPokeLabAppController {
    private DAOFactory daoFactory;
    private UserDAO userDAO;
    private PokeLabDAO pokeLabDAO;
//    private ColorDAO colorDAO;
//    private ExtraIngredientDAO extraIngredientDAO;

    public BuyPokeLabAppController() {
        daoFactory = DAOFactory.getInstance();
        userDAO = daoFactory.getUserDAO();
        pokeLabDAO = daoFactory.getPokeLabDAO();
//        colorDAO = daoFactory.getColorDAO();
//        extraIngredientDAO = daoFactory.getExtraIngredientDAO();
    }

    public PokeLabBean newPokeLab() {return new PokeLabBean();}

    public void addIngredient(PokeLabBean pokeLabBean, AddIngredientBean addIngredientBean){
        String ingredient = addIngredientBean.getIngredientName();
        GenericAlternative genericAlternative = addIngredientBean.getGenericAlternative();

        if (genericAlternative != null) {
            GenericAlternative oldIngredient = pokeLabBean.getIngredient(ingredient);
            if (oldIngredient != null) {
                pokeLabBean.setPrice(pokeLabBean.getPrice() - oldIngredient.price());
            }

            // aggiungo la nuova alternativa e aggiorno il prezzo
            pokeLabBean.setIngredient(ingredient, genericAlternative);
            pokeLabBean.setPrice(pokeLabBean.getPrice() + genericAlternative.price());
        }
    }

    private Ingredient allIngredient;

    public void registerPokeLab(Ingredient ingredient){this.allIngredient = ingredient;}

    //METTERE COSE SUL DECORATOR

    public boolean savePokeLab(PokeLabBean pokeLabBean, SaveBean saveBean){
        String uid = saveBean.getUid();

        if (!pokeLabBean.isFull()){
            System.out.println("Select all component!!!");
            return false;
        }

        System.out.println(pokeLabBean.getId());
        PokeLab pokeLab = new PokeLab(pokeLabBean);
        try{
            System.out.println(pokeLab.id());
            pokeLabDAO.create(pokeLab);
        } catch (SystemException e){
            throw new RuntimeException(e);
        }

        User user = null;
        try{
            user = userDAO.read(uid);
        } catch (SystemException e){
            throw new RuntimeException(e);
        }

        try {
            userDAO.update(user, pokeLab.id());
        } catch (SystemException e){
            throw new RuntimeException(e);
        }

        return true;
    }

    public boolean checkPokeLab(SaveBean saveBean){
        User user = null;
        String id = saveBean.getUid();
        try{
            user = userDAO.read(id);
        } catch (SystemException e){
            throw new RuntimeException(e);
        }

        PokeLab pokeLab = user.getPokeLab();
        return pokeLab != null;
    }

    //IMPLEMENTARE LA PARTE SUL RECUPERO DEI VECCHI POKE LAB MA NON SI POSSONO MODIFICARE
}
