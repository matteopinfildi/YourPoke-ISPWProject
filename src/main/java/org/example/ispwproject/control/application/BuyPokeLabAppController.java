package org.example.ispwproject.control.application;

import org.example.ispwproject.model.Ingredient;
import org.example.ispwproject.model.decorator.pokelab.PokeLab;
import org.example.ispwproject.model.decorator.pokelab.PokeLabDAO;
import org.example.ispwproject.model.decorator.spicy.SpicyDAO;
import org.example.ispwproject.model.decorator.spicy.SpicyDecorator;
import org.example.ispwproject.model.decorator.topping.ToppingDAO;
import org.example.ispwproject.model.decorator.topping.ToppingDecorator;
import org.example.ispwproject.model.user.User;
import org.example.ispwproject.model.user.UserDAO;
import org.example.ispwproject.utils.bean.AddIngredientBean;
import org.example.ispwproject.utils.bean.ExtraBean;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.dao.DAOFactory;
import org.example.ispwproject.utils.enumeration.Topping;
import org.example.ispwproject.utils.enumeration.ingredient.GenericAlternative;
import org.example.ispwproject.utils.exception.PokeLabSystemException;
import org.example.ispwproject.utils.exception.SystemException;

import java.util.*;

public class BuyPokeLabAppController {
    private DAOFactory daoFactory;
    private UserDAO userDAO;
    private PokeLabDAO pokeLabDAO;
    private ToppingDAO toppingDAO;
    private SpicyDAO spicyDAO;

    public BuyPokeLabAppController() {
        daoFactory = DAOFactory.getInstance();
        userDAO = daoFactory.getUserDAO();
        pokeLabDAO = daoFactory.getPokeLabDAO();
        toppingDAO = daoFactory.getToppingDAO();
        spicyDAO = daoFactory.getSpicyDAO();
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

    private Map<String, Integer> spicy = new HashMap<>();
    private List<Topping> topping = new ArrayList<>();

    public double putDecoration(PokeLabBean pokeLabBean, ExtraBean extraBean){
        PokeLab pokeLab = new PokeLab(pokeLabBean);
        registerPokeLab(pokeLab);

        spicy = extraBean.getSpicyMap();
        for (var entry : spicy.entrySet()){
            String key = entry.getKey();
            Integer v = entry.getValue();
            for (int i=0; i < v; i++){
                allIngredient = new SpicyDecorator(allIngredient, key);
            }
        }

        boolean[][] toppings = extraBean.getTopping();
        topping = new ArrayList<>(Arrays.asList(Topping.values()));
        for (int x = 0; x < toppings.length; x++) {
            // se il topping è selezionato
            if (toppings[x][0]) {
                boolean crispy = toppings[x][1];
                allIngredient = new ToppingDecorator(allIngredient, topping.get(x), crispy);
            }
        }
        return allIngredient.price();
    }

    public boolean savePokeLab(PokeLabBean pokeLabBean, SaveBean saveBean) throws PokeLabSystemException {
        String uid = saveBean.getUid();

        if (!pokeLabBean.isFull()){
            System.out.println("Select all component!!!");
            return false;
        }

        // aggiungo il nome del poke al pokeLab
        String pokeName = pokeLabBean.getPokeName();
        String bowlSize = pokeLabBean.getBowlSize();
        System.out.println("Saving Poké with name: " + pokeName);
        System.out.println("Saving Poké with size: " + bowlSize);

        System.out.println(pokeLabBean.getId());
        PokeLab pokeLab = new PokeLab(pokeLabBean);
        try{
            System.out.println(pokeLab.id());
            pokeLabDAO.create(pokeLab);
        } catch (SystemException e){
            throw new PokeLabSystemException("Error", e);
        }

        User user = null;
        try{
            user = userDAO.read(uid);
        } catch (SystemException e){
            throw new PokeLabSystemException("Error", e);
        }

        try {
            userDAO.update(user, pokeLab.id());
        } catch (SystemException e){
            throw new PokeLabSystemException("Error", e);
        }

        return true;
    }

    public boolean checkPokeLab(SaveBean saveBean) throws PokeLabSystemException {
        User user = null;
        String id = saveBean.getUid();
        try{
            user = userDAO.read(id);
        } catch (SystemException e){
            throw new PokeLabSystemException("Error", e);
        }

        PokeLab pokeLab = user.getPokeLab();
        return pokeLab != null;
    }

    public PokeLabBean recoverPokeLab(SaveBean saveBean) throws PokeLabSystemException {
        User user = null;
        String uID = saveBean.getUid();
        try{
            user = userDAO.read(uID);
        } catch (SystemException systemException){
            throw new PokeLabSystemException("Error", systemException);
        }

        PokeLab pokeLab = user.getPokeLab();
        if (pokeLab != null) {
            return new PokeLabBean(pokeLab);
        }
        return null;
    }

    public void updateBowlSize(PokeLabBean pokeLabBean) throws PokeLabSystemException {
        try {
            pokeLabDAO.updateBowlSize(pokeLabBean.getId(), pokeLabBean.getBowlSize());
        } catch (SystemException e) {
            throw new PokeLabSystemException("Error", e);
        }
    }


}
