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
        // prendo l'ingrediente su cui voglio agire e l'eventuale alternativa già esistente
        String ingredient = addIngredientBean.getIngredientName();
        GenericAlternative genericAlternative = addIngredientBean.getGenericAlternative();

        // elimina un vecchio ingrediente andando a modificare anche il prezzo
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


    // salva il poke lab nuovo, associandolo all'utente ed eliminando un eventuale poke vecchio
    public boolean savePokeLab(PokeLabBean pokeLabBean, SaveBean saveBean) throws SystemException {
        if (pokeLabBean.getId() > 0) {
            // già salvato in precedenza, allora effettua update
            PokeLab poke = new PokeLab(pokeLabBean);
            pokeLabDAO.update(poke);
            return true;

        }
        // altrimenti crea il primo PokeLab
        PokeLab pokeLab = new PokeLab(pokeLabBean);
        pokeLabDAO.create(pokeLab);
        // recupero l'id generato dallo strato di persistenza per controllare se è valido
        if (pokeLab.id() <= 0) {
            throw new SystemException("Invalid PokeLab ID generated");
        }
        pokeLabBean.setId(pokeLab.id());
        // lettura dell'utente e controllo validità
        User user = userDAO.read(saveBean.getUid());
        if (user == null) {
            throw new SystemException("User not found: " + saveBean.getUid());
        }
        // aggiornamento coppia (user, pokeLab)
        userDAO.update(user, pokeLab.id());
        return true;
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
        // il metodo trim() è un metodo che rimuove gli spazi bianchi iniziali e finali da una stringa
        if (name == null || name.trim().length() < 4) {
            throw new SystemException("The name must be at least 4 characters long!");
        }
        /* controllo se il nome inserito è già uguale ad un eventuale altro nome già assegnato allo stesso poke
           così da non fare altri salvataggi inutili*/
        if (name.equals(pokeLabBean.getPokeName())) {
            return true;
        }
        pokeLabBean.setPokeName(name);
        return savePokeLab(pokeLabBean, saveBean);
    }

    // imposta/aggiorna la size della bowl se è diverso dal nome assegnato in precedenza
    public boolean setBowlSize(PokeLabBean pokeLabBean, String bowlSize) throws SystemException {
        if (bowlSize == null || bowlSize.isEmpty()) {
            throw new SystemException("Bowl size cannot be empty");
        }
        /* controllo se la size inserita è già uguale ad un eventuale altra size già assegnata allo stesso poke
           così da non fare altri salvataggi inutili*/
        if (bowlSize.equals(pokeLabBean.getBowlSize())) {
            return true;
        }
        pokeLabBean.setBowlSize(bowlSize);
        return true;
    }

}
