package org.example.ispwproject.utils.dao;


import org.example.ispwproject.model.user.InMemoryUserDAO;
import org.example.ispwproject.model.user.UserDAO;

public class InMemoryDAOFactory extends DAOFactory{

//    @Override
//    public PokelabDAO getPokeLabDAO(){return InMemoryPokeLabDAO.getInstance();}
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
