package org.example.ispwproject.utils.dao;

import org.example.ispwproject.model.pokelab.DBPokeLabDAO;
import org.example.ispwproject.model.pokelab.PokeLabDAO;
import org.example.ispwproject.model.observer.pokewall.DBPokeWallDAO;
import org.example.ispwproject.model.observer.pokewall.PokeWallDAO;
import org.example.ispwproject.model.user.DBUserDAO;
import org.example.ispwproject.model.user.UserDAO;

public class DBDAOFactory extends DAOFactory{
    @Override
    public PokeLabDAO getPokeLabDAO(){return new DBPokeLabDAO();}

    @Override
    public UserDAO getUserDAO(){return new DBUserDAO();}

    @Override
    public PokeWallDAO getPokeWallDAO() {return new DBPokeWallDAO();}
}
