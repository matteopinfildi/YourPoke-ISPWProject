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



    public boolean savePokeLab(PokeLabBean pokeLabBean, SaveBean saveBean) throws SystemException {
        try {
            // 1. Crea il PokeLab (il DAO gestirà la transazione internamente)
            PokeLab pokeLab = new PokeLab(pokeLabBean);
            pokeLabDAO.create(pokeLab); // Senza passare la connection

            // 2. Verifica che l'ID sia stato generato
            if (pokeLab.id() <= 0) {
                throw new SystemException("Invalid PokeLab ID generated");
            }

            // 3. Recupera l'utente
            User user = userDAO.read(saveBean.getUid());
            if (user == null) {
                throw new SystemException("User not found: " + saveBean.getUid());
            }

            // 4. Elimina eventuale PokeLab precedente
            if (user.getPokeLab() != null) {
                pokeLabDAO.delete(user.getPokeLab().id());
            }

            // 5. Aggiorna riferimento al nuovo PokeLab
            userDAO.update(user, pokeLab.id());

            return true;

        } catch (SystemException e) {
            throw new SystemException("Failed to save PokeLab: " + e.getMessage());
        }
    }

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

    public boolean setPokeName(PokeLabBean pokeLabBean,
                                  String name,
                                  SaveBean saveBean) throws SystemException {
        // 1. Validazione
        if (name == null || name.trim().length() < 4) {
            throw new SystemException("The name must be at least 4 characters long!");
        }
        // 2. Se non cambia, esci subito
        if (name.equals(pokeLabBean.getPokeName())) {
            return true;
        }
        // 3. Applica il nuovo nome sulla bean
        pokeLabBean.setPokeName(name);
        // 4. Salva tutta la bean richiamando il metodo esistente
        return savePokeLab(pokeLabBean, saveBean);
    }

    public boolean setBowlSize(PokeLabBean pokeLabBean,
                                  String bowlSize,
                                  SaveBean saveBean) throws SystemException {
        // (1) Validazione minima: ad esempio, non vuoto
        if (bowlSize == null || bowlSize.isEmpty()) {
            throw new SystemException("Bowl size cannot be empty");
        }
        // (2) Se non cambia, esci subito
        if (bowlSize.equals(pokeLabBean.getBowlSize())) {
            return true;
        }
        // (3) Applica sulla bean
        pokeLabBean.setBowlSize(bowlSize);
        // (4) Salva con il metodo esistente
        return savePokeLab(pokeLabBean, saveBean);
    }

}
