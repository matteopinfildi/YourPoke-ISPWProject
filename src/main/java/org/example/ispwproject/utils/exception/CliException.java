package org.example.ispwproject.utils.exception;

// eccezione utilizzata per rappresentare errori che si verificano nell'interfaccia CLI
public class CliException extends Exception {
    public CliException(String message, Throwable cause) {
        super(message, cause);
    }
}