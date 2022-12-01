package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * Exception for errors in the game.
 */
public class SplendorGameException extends Exception {

  public SplendorGameException(String reason) {
    super(reason);
  }

}