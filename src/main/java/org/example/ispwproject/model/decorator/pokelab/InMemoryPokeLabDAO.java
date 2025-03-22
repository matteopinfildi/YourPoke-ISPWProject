package org.example.ispwproject.model.decorator.pokelab;

import org.example.ispwproject.utils.exception.SystemException;

import java.util.ArrayList;
import java.util.Collection;

public class InMemoryPokeLabDAO implements PokeLabDAO {
    private static InMemoryPokeLabDAO instance;
    private Collection<PokeLab> listOfPokeLab = new ArrayList<>();

    private InMemoryPokeLabDAO(){}

    public static InMemoryPokeLabDAO getInstance(){
        if (instance == null) {
            instance = new InMemoryPokeLabDAO();
        }
        return instance;
    }

    @Override
    public void create(PokeLab pokeLab) throws SystemException {
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
        try {
            listOfPokeLab.remove(read(plid));
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

}
