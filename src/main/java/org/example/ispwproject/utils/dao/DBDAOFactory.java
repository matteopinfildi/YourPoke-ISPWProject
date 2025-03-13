package org.example.ispwproject.utils.dao;

import org.example.ispwproject.model.decorator.InMemoryPokeLabDAO;
import org.example.ispwproject.model.decorator.PokeLabDAO;
import org.example.ispwproject.model.user.InMemoryUserDAO;
import org.example.ispwproject.model.user.UserDAO;

public class DBDAOFactory extends DAOFactory{
    @Override
    public PokeLabDAO getPokeLabDAO(){return InMemoryPokeLabDAO.getInstance();}
//
//    @Override
//    public ColorDAO getColorDAO(){return InMemoryColorDAO.getIstance();}
//
//    @Override
//    public ExtraIngredientDAO getExtraIngredientDAO(){return InMemoryExtraIngredientDAO.getIstance();}
//
//    @Override
    public UserDAO getUserDAO(){return InMemoryUserDAO.getInstance();}
}
