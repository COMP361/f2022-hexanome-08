package ca.group8.gameservice.splendorgame.controller.splendorlogic;

/**
 * Exception for breaking game logic.
 */
public class SplendorLogicException extends Exception {
  /**
   * Constructor.
   *
   * @param reason of exception
   */
  public SplendorLogicException(String reason) {
    super(reason);
  }
}
