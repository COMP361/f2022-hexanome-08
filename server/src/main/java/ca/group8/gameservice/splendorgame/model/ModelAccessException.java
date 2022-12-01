package ca.group8.gameservice.splendorgame.model;

/**
 * Custom Exception that is fired whenever model modifications are
 * requested that would lead to an inconsistent
 * state.
 *
 * @Author: Maximilian Schiedermeier
 * @Date: December 2020
 */
public class ModelAccessException extends Exception {
  public ModelAccessException(String cause) {
    super(cause);
  }
}
