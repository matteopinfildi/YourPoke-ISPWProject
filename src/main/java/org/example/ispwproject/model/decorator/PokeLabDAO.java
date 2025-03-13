package org.example.ispwproject.model.decorator;

import org.example.ispwproject.utils.exception.SystemException;

public interface PokeLabDAO {
    void create(PokeLab pokeLab) throws SystemException;
    PokeLab read(int plid) throws  SystemException;
    public void delete(int plid) throws SystemException;
}
