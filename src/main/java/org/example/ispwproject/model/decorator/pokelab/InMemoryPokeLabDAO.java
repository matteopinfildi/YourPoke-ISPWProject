//package org.example.ispwproject.model.decorator.pokelab;
//
//import org.example.ispwproject.utils.exception.SystemException;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//public class InMemoryPokeLabDAO implements PokeLabDAO {
//    private static InMemoryPokeLabDAO instance;
//    private Collection<PokeLab> listOfPokeLab = new ArrayList<>();
//
//    private InMemoryPokeLabDAO(){}
//
//    public static synchronized InMemoryPokeLabDAO getInstance(){
//        if (instance == null) {
//            instance = new InMemoryPokeLabDAO();
//        }
//        return instance;
//    }
//
//    @Override
//    public void create(PokeLab pokeLab) throws SystemException {
//        if (pokeLab == null) {
//            throw new SystemException("Error");
//        }
//        listOfPokeLab.add(pokeLab);
//    }
//
//    @Override
//    public PokeLab read(int plid) throws SystemException {
//        for (PokeLab pokeLab: listOfPokeLab) {
//            if (pokeLab.id() == plid) {
//                return pokeLab;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public void delete(int plid) throws SystemException {
//        boolean removed = listOfPokeLab.removeIf(pokeLab -> pokeLab.id() == plid);
//        if (!removed) {
//            throw new SystemException("PokéLab con id " + plid + " non trovato.");
//        }
//    }
//
//    @Override
//    public void updateBowlSize(int plid, String bowlSize) throws SystemException {
//        PokeLab pokeLab = read(plid);
//        if (pokeLab != null) {
//            pokeLab.setBowlSize(bowlSize);  // Aggiorna la bowl size
//        } else {
//            throw new SystemException("PokéLab con id " + plid + " non trovato.");
//        }
//    }
//
//}

package org.example.ispwproject.model.decorator.pokelab;

import org.example.ispwproject.utils.exception.SystemException;
import java.util.ArrayList;
import java.util.Collection;

public class InMemoryPokeLabDAO implements PokeLabDAO {

    // Using the "Initialization-on-demand holder" idiom for thread-safe lazy initialization
    private static class Holder {
        static final InMemoryPokeLabDAO INSTANCE = new InMemoryPokeLabDAO();
    }

    private final Collection<PokeLab> listOfPokeLab = new ArrayList<>();

    private InMemoryPokeLabDAO() {
        // Prevent instantiation via reflection
        if (Holder.INSTANCE != null) {
            throw new IllegalStateException("Singleton already initialized");
        }
    }

    public static InMemoryPokeLabDAO getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void create(PokeLab pokeLab) throws SystemException {
        if (pokeLab == null) {
            throw new SystemException("Error");
        }
        listOfPokeLab.add(pokeLab);
    }

    @Override
    public PokeLab read(int plid) throws SystemException {
        for (PokeLab pokeLab: listOfPokeLab) {
            if (pokeLab.id() == plid) {
                return pokeLab;
            }
        }
        return null;
    }

    @Override
    public void delete(int plid) throws SystemException {
        boolean removed = listOfPokeLab.removeIf(pokeLab -> pokeLab.id() == plid);
        if (!removed) {
            throw new SystemException("PokéLab con id " + plid + " non trovato.");
        }
    }

    @Override
    public void updateBowlSize(int plid, String bowlSize) throws SystemException {
        PokeLab pokeLab = read(plid);
        if (pokeLab != null) {
            pokeLab.setBowlSize(bowlSize);
        } else {
            throw new SystemException("PokéLab con id " + plid + " non trovato.");
        }
    }
}
