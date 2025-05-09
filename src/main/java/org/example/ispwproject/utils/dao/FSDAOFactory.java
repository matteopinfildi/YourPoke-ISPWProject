package org.example.ispwproject.utils.dao;

import org.example.ispwproject.model.decorator.pokelab.FSPokeLabDAO;
import org.example.ispwproject.model.decorator.pokelab.PokeLabDAO;
import org.example.ispwproject.model.observer.pokewall.FSPokeWallDAO;
import org.example.ispwproject.model.observer.pokewall.PokeWallDAO;
import org.example.ispwproject.model.user.FSUserDAO;
import org.example.ispwproject.model.user.UserDAO;

public class FSDAOFactory extends DAOFactory{
    @Override
    public PokeLabDAO getPokeLabDAO(){return new FSPokeLabDAO();}

    @Override
    public UserDAO getUserDAO(){return new FSUserDAO();}

    @Override
    public PokeWallDAO getPokeWallDAO() {return new FSPokeWallDAO();}
}
