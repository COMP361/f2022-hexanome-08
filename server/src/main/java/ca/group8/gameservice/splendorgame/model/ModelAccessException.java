package ca.group8.gameservice.splendorgame.model;

/**
 * Custom Exception that is fired whenever model modifications are
 * requested that would lead to an inconsistent
 * state.
 */
public class ModelAccessException extends Exception {
  public ModelAccessException(String cause) {
    super(cause);
  }
}
