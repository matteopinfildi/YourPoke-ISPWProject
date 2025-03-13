package org.example.ispwproject.utils.enumeration;

import org.example.ispwproject.utils.dao.DAOFactory;
import org.example.ispwproject.utils.dao.DBDAOFactory;
import org.example.ispwproject.utils.dao.FSDAOFactory;
import org.example.ispwproject.utils.dao.InMemoryDAOFactory;

public enum PersistenceProvider {
    DB(DBDAOFactory.class),
    FS(FSDAOFactory.class),
    IN_MEMORY(InMemoryDAOFactory.class);

    private final Class<? extends DAOFactory> daoFactoryClass;

    PersistenceProvider(Class<? extends DAOFactory> daoFactoryClass) {
        this.daoFactoryClass = daoFactoryClass;
    }

    public Class<? extends DAOFactory> getDaoFactoryClass() {return daoFactoryClass;}

}
