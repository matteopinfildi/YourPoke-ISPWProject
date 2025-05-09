package org.example.ispwproject.utils.dao;


import org.example.ispwproject.model.decorator.pokelab.InMemoryPokeLabDAO;
import org.example.ispwproject.model.decorator.pokelab.PokeLabDAO;
import org.example.ispwproject.model.observer.pokewall.InMemoryPokeWallDAO;
import org.example.ispwproject.model.observer.pokewall.PokeWallDAO;
import org.example.ispwproject.model.user.InMemoryUserDAO;
import org.example.ispwproject.model.user.UserDAO;

public class InMemoryDAOFactory extends DAOFactory{

    @Override
    public PokeLabDAO getPokeLabDAO(){return InMemoryPokeLabDAO.getInstance();}

    @Override
    public UserDAO getUserDAO(){return InMemoryUserDAO.getInstance();}

    @Override
    public PokeWallDAO getPokeWallDAO() {return InMemoryPokeWallDAO.getInstance();}
}
