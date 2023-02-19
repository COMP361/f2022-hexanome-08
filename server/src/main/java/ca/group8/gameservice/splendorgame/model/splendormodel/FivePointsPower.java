package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * This class represents the Five extra prestige points power.
 */
public class FivePointsPower extends Power {

  public FivePointsPower() {
    super(PowerEffect.FIVE_POINTS);
  }

  @Override
  boolean validityCheck(PlayerInGame playerInfo) {
    return false;
  }
}
