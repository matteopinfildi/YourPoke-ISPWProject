package org.example.ispwproject.model.decorator.pokelab;

import org.example.ispwproject.utils.exception.SystemException;

public class FSPokeLabDAO implements PokeLabDAO {
    @Override
    public void create(PokeLab pokeLab) throws SystemException {
        // da implementare
    }

    @Override
    public PokeLab read(int plid) throws SystemException {
        return null;
    }

    @Override
    public void delete(int plid) throws SystemException {
        // da implementare
    }

    @Override
    public void updateBowlSize(int plid, String bowlSize){}
}
