package org.example.ispwproject.control.application;

import org.example.ispwproject.model.pokelab.PokeLab;
import org.example.ispwproject.model.pokelab.PokeLabDAO;
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

    public BuyPokeLabAppController() {
        daoFactory = DAOFactory.getInstance();
        userDAO = daoFactory.getUserDAO();
        pokeLabDAO = daoFactory.getPokeLabDAO();
    }

    // crea e restituisce una nuova istanza vuota della bean
    public PokeLabBean newPokeLab() {return new PokeLabBean();}

    // aggiunge/sostituisce un ingrediente al poke e aggiorna il totale del prezzo
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


    // salva il poke lab nuovo, associandolo all'utente ed eliminando quello vecchio
    public boolean savePokeLab(PokeLabBean pokeLabBean, SaveBean saveBean) throws SystemException {
        try {
            PokeLab pokeLab = new PokeLab(pokeLabBean);
            pokeLabDAO.create(pokeLab);

            if (pokeLab.id() <= 0) {
                throw new SystemException("Invalid PokeLab ID generated");
            }

            User user = userDAO.read(saveBean.getUid());
            if (user == null) {
                throw new SystemException("User not found: " + saveBean.getUid());
            }

            if (user.getPokeLab() != null) {
                pokeLabDAO.delete(user.getPokeLab().id()); // elimina il vecchio poke (se esiste)
            }

            userDAO.update(user, pokeLab.id()); // aggiorna nuovo poke

            return true;

        } catch (SystemException e) {
            throw new SystemException("Failed to save PokeLab: " + e.getMessage());
        }
    }

    // verifica se l'utente ha già un poke salvato
    public boolean checkPokeLab(SaveBean saveBean) throws SystemException {
        User user = null;
        String id = saveBean.getUid();
        try{
            user = userDAO.read(id);
        } catch (SystemException e){
            throw new SystemException("Error" + e.getMessage());
        }

        PokeLab pokeLab = user.getPokeLab();
        return pokeLab != null;
    }

    // recupera il poke precedentemente salvato per l'utente
    public PokeLabBean recoverPokeLab(SaveBean saveBean) throws SystemException {
        User user = null;
        String uID = saveBean.getUid();
        try{
            user = userDAO.read(uID);
        } catch (SystemException e){
            throw new SystemException("Error" + e.getMessage());
        }

        PokeLab pokeLab = user.getPokeLab();
        if (pokeLab != null) {
            return new PokeLabBean(pokeLab);
        }
        return null;
    }

    // imposta/aggiorna il nome del poke, se ha almeno 4 lettere e se è diverso dal nome assegnato in precedenza
    public boolean setPokeName(PokeLabBean pokeLabBean, String name, SaveBean saveBean) throws SystemException {
        if (name == null || name.trim().length() < 4) {
            throw new SystemException("The name must be at least 4 characters long!");
        }
        if (name.equals(pokeLabBean.getPokeName())) {
            return true;
        }
        pokeLabBean.setPokeName(name);
        return savePokeLab(pokeLabBean, saveBean);
    }

    // imposta/aggiorna la size della bowl se è diverso dal nome assegnato in precedenza
    public boolean setBowlSize(PokeLabBean pokeLabBean, String bowlSize, SaveBean saveBean) throws SystemException {
        if (bowlSize == null || bowlSize.isEmpty()) {
            throw new SystemException("Bowl size cannot be empty");
        }
        if (bowlSize.equals(pokeLabBean.getBowlSize())) {
            return true;
        }
        pokeLabBean.setBowlSize(bowlSize);
        return savePokeLab(pokeLabBean, saveBean);
    }

}
