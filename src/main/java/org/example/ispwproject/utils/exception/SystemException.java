package org.example.ispwproject.utils.exception;

public class SystemException extends Exception {
  public SystemException() {
    super("Oh no! Somehing wrong!");
  }

  public SystemException(String message) {
    super("Technical error.\n" + message);
  }
}
