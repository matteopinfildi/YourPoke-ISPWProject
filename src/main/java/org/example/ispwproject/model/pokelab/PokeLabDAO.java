package org.example.ispwproject.model.pokelab;

import org.example.ispwproject.utils.exception.SystemException;

public interface PokeLabDAO {
    void create(PokeLab pokeLab) throws SystemException;
    PokeLab read(int plid) throws  SystemException;
    public void delete(int plid) throws SystemException;
    void updateBowlSize(int plid, String bowlSize) throws SystemException;
    void update(PokeLab pokeLab) throws SystemException;
}
