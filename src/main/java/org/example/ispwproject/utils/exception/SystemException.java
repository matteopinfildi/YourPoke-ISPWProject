package org.example.ispwproject.utils.exception;

// eccezione usata per indicare errori generici all'interno del sistema
public class SystemException extends Exception {
  public SystemException() {
    super("Somehing wrong!");
  }

  public SystemException(String message) {
    super("Error.\n" + message);
  }
}
