package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * Exception for errors in the game.
 */
public class SplendorGameException extends Exception {

  /**
   * Constructor of exception.
   *
   * @param reason String of reason
   */
  public SplendorGameException(String reason) {
    super(reason);
  }

}