package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * This class represents the Power functionality in the Trading Post Extension.
 */
public abstract class Power {

  private boolean unlocked;
  private final PowerEffect powerEffect;
  private final PlayerInGame playerInfo;

  /**
   * The constructs a new power object, which is tied to a specific Player.
   *
   * @param powerEffect An enum representing which power this object is.
   * @param playerInfo The player associated to this instance of a power.
   */
  public Power(PowerEffect powerEffect,
               PlayerInGame playerInfo) {
    this.unlocked = false; //default value
    this.powerEffect = powerEffect;
    this.playerInfo = playerInfo;
  }

  /**
   * This method checks to see if a player meets the qualifications to unlock this power.
   *
   * @param playerInfo the Player whose qualifications we are checking.
   * @return a boolean of whether the Player can unlock this power.
   */
  abstract boolean validityCheck(PlayerInGame playerInfo);

  public boolean isUnlocked() {return unlocked;};

  //Do not make these methods [unlock() & lock()]public as they should only be called directly by
  // an inheriting power class (ex. TwoPlusOnePower).
  void unlock() {unlocked=true;}

  void lock() {unlocked=false;}
}
