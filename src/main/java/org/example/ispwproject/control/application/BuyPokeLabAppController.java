package org.example.ispwproject.control.application;

import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.model.pokelab.PokeLab;
import org.example.ispwproject.model.pokelab.PokeLabDAO;
import org.example.ispwproject.model.user.User;
import org.example.ispwproject.model.user.UserDAO;
import org.example.ispwproject.utils.bean.AddIngredientBean;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;
import org.example.ispwproject.utils.dao.DAOFactory;
import org.example.ispwproject.utils.enumeration.ingredient.GenericOption;
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
        GenericOption genericOption = addIngredientBean.getGenericAlternative();

        // elimina un vecchio ingrediente andando a modificare anche il prezzo e le calorie
        if (genericOption != null) {
            // recupero una alternativa vecchia e controllo se esiste
            GenericOption oldIngredient = pokeLabBean.getIngredient(ingredient);
            if (oldIngredient != null) {
                // sottraggo il prezzo
                pokeLabBean.setPrice(pokeLabBean.getPrice() - oldIngredient.price());
                // sottraggo le calorie
                pokeLabBean.setCalories(pokeLabBean.getCalories() - oldIngredient.calories());
            }

            // aggiungo/aggiorno la nuova alternativa
            pokeLabBean.setIngredient(ingredient, genericOption);
            // aggiorno il prezzo totale
            pokeLabBean.setPrice(pokeLabBean.getPrice() + genericOption.price());
            // aggiorno le calorie totali
            pokeLabBean.setCalories(pokeLabBean.getCalories() + genericOption.calories());
        }
    }


    // salva il poke lab nuovo, associandolo all'utente ed eliminando un eventuale poke vecchio
    public boolean savePokeLab(PokeLabBean pokeLabBean, SaveBean saveBean) throws SystemException {
        PokeLab pokeLab = new PokeLab(pokeLabBean);

        if (pokeLabBean.getId() > 0) {
            // già salvato in precedenza, allora effettua update
            pokeLabDAO.update(pokeLab);
            return true;

        }
        // altrimenti crea il nuovo PokeLab
        pokeLabDAO.create(pokeLab);
        // recupero l'id generato dallo strato di persistenza per controllare se è valido
        if (pokeLab.id() <= 0) {
            throw new SystemException("Invalid PokeLab ID generated");
        }
        // aggiorno la bean con l'id appena creato
        pokeLabBean.setId(pokeLab.id());
        // lettura dell'utente e controllo se esiste
        User user = userDAO.read(saveBean.getUid());
        if (user == null) {
            throw new SystemException("User not found: " + saveBean.getUid());
        }
        // aggiornamento coppia (user, pokeLab), per cui associo un nuovo PokeLab all'utente
        userDAO.update(user, pokeLab.id());
        return true;
    }

    // verifica se l'utente ha già un poke salvato
    public boolean checkPokeLab(SaveBean saveBean) throws SystemException {
        User user = null;
        // recupero l'id dell'utente a cui vogliamo controllare se ha già un poke salvato
        String id = saveBean.getUid();
        try{
            // recupero l'utente dallo strato di persistenza
            user = userDAO.read(id);
        } catch (SystemException e){
            throw new SystemException("Error" + e.getMessage());
        }
        /* se l'utente ha già un poke salvato, questo campo conterrà un riferimento ad un'istanza di PokeLab,
         altrimenti conterrà null */
        PokeLab pokeLab = user.getPokeLab();
        // restituisce true se pokeLab != null, altrimenti restituisce false
        return pokeLab != null;
    }

    // recupera il poke precedentemente salvato per l'utente
    public PokeLabBean retrievePokeLab(SaveBean saveBean) throws SystemException {
        User user = null;
        // recupero l'id dell'utente a cui vogliamo recuperare il poke già salvato
        String uID = saveBean.getUid();
        try{
            // recupero l'utente dallo strato di persistenza
            user = userDAO.read(uID);
        } catch (SystemException e){
            throw new SystemException("Error" + e.getMessage());
        }

        // dall'oggetto user, recupero il campo PokeLab, che se non esiste, ci darà null
        PokeLab pokeLab = user.getPokeLab();
        if (pokeLab != null) {
            return new PokeLabBean(pokeLab);
        }
        return null;
    }

    // recupera la PokeLabBean associata all’utente loggato nella sessione specificata
    public PokeLabBean retrievePokeLabBySession(int sessionId) throws SystemException {
        // recupero la sessione
        Session session = SessionManager.getSessionManager().getSessionFromId(sessionId);
        if (session == null) {
            throw new SystemException("Session not found");
        }
        // estraggo l'id dell'utente dalla sessione
        String userId = session.getUserId();
        return retrievePokeLab(new SaveBean(userId));
    }

    // imposta/aggiorna il nome del poke, se ha almeno 4 lettere e se è diverso dal nome assegnato in precedenza
    public boolean setPokeName(PokeLabBean pokeLabBean, String name, int sessionId) throws SystemException {
        // si assegna alla bean il nuovo nome passato come parametro
        pokeLabBean.setPokeName(name);
        // recupero la sessione
        Session session = SessionManager.getSessionManager().getSessionFromId(sessionId);
        if (session == null) {
            throw new SystemException("Session not found. Please log in again.");
        }
        // estraggo l'id dell'utente dalla sessione
        String userId = session.getUserId();

        return savePokeLab(pokeLabBean, new SaveBean(userId));
    }

    // imposta/aggiorna la size della bowl se è diverso dal nome assegnato in precedenza
    public boolean setBowlSize(PokeLabBean pokeLabBean, String bowlSize, int sessionId) throws SystemException {
        // si assegna alla bean la nuova size
        pokeLabBean.setBowlSize(bowlSize);
        // recupero la sessione
        Session session = SessionManager.getSessionManager().getSessionFromId(sessionId);
        if (session == null) {
            throw new SystemException("Session not found. Please log in again.");
        }
        // estraggo l'id dell'utente dalla sessione
        String userId = session.getUserId();

        return savePokeLab(pokeLabBean, new SaveBean(userId));
    }

}
