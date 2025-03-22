package org.example.ispwproject.utils.dao;

import org.example.ispwproject.model.decorator.pokelab.InMemoryPokeLabDAO;
import org.example.ispwproject.model.decorator.pokelab.PokeLabDAO;
import org.example.ispwproject.model.user.InMemoryUserDAO;
import org.example.ispwproject.model.user.UserDAO;

public class FSDAOFactory extends DAOFactory{
    @Override
    public PokeLabDAO getPokeLabDAO(){return InMemoryPokeLabDAO.getInstance();}
//
//    @Override
//    public ColorDAO getColorDAO(){return InMemoryColorDAO.getIstance();}
//
//    @Override
//    public ExtraIngredientDAO getExtraIngredientDAO(){return InMemoryExtraIngredientDAO.getIstance();}
//
    @Override
    public UserDAO getUserDAO(){return InMemoryUserDAO.getInstance();}
}
