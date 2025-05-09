package org.example.ispwproject.utils.dao;

import org.example.ispwproject.model.decorator.pokelab.PokeLabDAO;
import org.example.ispwproject.model.observer.pokewall.PokeWallDAO;
import org.example.ispwproject.model.user.UserDAO;
import org.example.ispwproject.utils.enumeration.PersistenceProvider;
import org.example.ispwproject.utils.exception.PersistenceProviderNotFoundException;

import java.lang.reflect.InvocationTargetException;

public abstract class DAOFactory {
    private static DAOFactory daoFactoryIstance = null;
    private static PersistenceProvider persistenceProvider = null;

    protected DAOFactory() {}

    public abstract PokeLabDAO getPokeLabDAO();
    public abstract UserDAO getUserDAO();
    public abstract PokeWallDAO getPokeWallDAO();


    public static DAOFactory getInstance() throws PersistenceProviderNotFoundException {
        if (daoFactoryIstance == null) {
            try {
                daoFactoryIstance = persistenceProvider.getDaoFactoryClass().getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
                     InvocationTargetException | NoSuchMethodException | SecurityException e ) {
                throw new PersistenceProviderNotFoundException(persistenceProvider, e);
            }
            return daoFactoryIstance;
        }
        return daoFactoryIstance;
    }
    public static void setPersistenceProvider(PersistenceProvider provider){persistenceProvider = provider;}

}
