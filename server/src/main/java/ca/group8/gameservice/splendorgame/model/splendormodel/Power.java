package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * This class represents the Power functionality in the Trading Post Extension.
 */
public abstract class Power {

  boolean unlocked;
  private final PowerEffect powerEffect;

  /**
   * The constructs a new power object, which is tied to a specific Player.
   *
   * @param powerEffect An enum representing which power this object is.
   */
  public Power(PowerEffect powerEffect) {
    this.unlocked = false; //default value
    this.powerEffect = powerEffect;
  }

  /**
   * This method checks to see if a player meets the qualifications to unlock this power.
   *
   * @param playerInfo the Player whose qualifications we are checking.
   * @return a boolean value of whether the Player can unlock this power.
   */
  abstract boolean validityCheck(PlayerInGame playerInfo);

  public boolean isUnlocked() {
    return unlocked;
  }

  //Do not make these methods [unlock() & lock()]public as they should only be called directly by
  // an inheriting power class (ex. TwoPlusOnePower).
  void unlock() {
    unlocked = true;
  }

  void lock() {
    unlocked = false;
  }
}
