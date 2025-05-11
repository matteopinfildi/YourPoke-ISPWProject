package org.example.ispwproject.model.pokelab;

import org.example.ispwproject.utils.exception.SystemException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryPokeLabDAO implements PokeLabDAO {
    private static InMemoryPokeLabDAO instance;
    private Collection<PokeLab> listOfPokeLab = new ArrayList<>();
    private AtomicInteger idCounter = new AtomicInteger(1);

    private InMemoryPokeLabDAO(){}

    public static synchronized InMemoryPokeLabDAO getInstance(){
        if (instance == null) {
            instance = new InMemoryPokeLabDAO();
        }
        return instance;
    }

    @Override
    public void create(PokeLab pokeLab) throws SystemException {
        if (pokeLab == null) {
            throw new SystemException("PokeLab cannot be null");
        }

        // Genera e assegna ID come nella versione DB
        int newId = idCounter.getAndIncrement();
        pokeLab.setId(newId);

        listOfPokeLab.add(pokeLab);
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

    // In InMemoryPokeLabDAO
    @Override
    public void updateBowlSize(int plid, String bowlSize) throws SystemException {
        PokeLab pokeLab = read(plid);
        if (pokeLab == null) {
            throw new SystemException("PokéLab con id " + plid + " non trovato.");
        }
        pokeLab.setBowlSize(bowlSize);
    }

}


