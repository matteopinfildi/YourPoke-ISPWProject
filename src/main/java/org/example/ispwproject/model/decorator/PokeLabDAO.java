package org.example.ispwproject.model.decorator;

import org.example.ispwproject.utils.exception.SystemException;

public interface PokeLabDAO {
    void create(PokeLab pokeLab) throws SystemException;
    PokeLab read(int pokeLabID) throws  SystemException;

}
