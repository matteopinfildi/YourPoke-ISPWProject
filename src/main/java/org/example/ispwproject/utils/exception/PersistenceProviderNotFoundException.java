package org.example.ispwproject.utils.exception;

import org.example.ispwproject.utils.enumeration.PersistenceProvider;

public class PersistenceProviderNotFoundException extends RuntimeException {
    public PersistenceProviderNotFoundException(PersistenceProvider persistenceProvider, Exception e) {
        super("Impossible to instantiate factory for provider " + persistenceProvider.getClass().getName(), e);
    }
}
