package project.view;

/**
 * Custom Exception that is fired whenever model modifications are
 * requested that would lead to an inconsistent
 * state.
 */
public class InvalidDataException extends Exception {
  public InvalidDataException(String cause) {
    super(cause);
  }
}
