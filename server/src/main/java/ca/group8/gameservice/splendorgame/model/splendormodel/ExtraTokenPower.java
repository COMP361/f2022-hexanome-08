package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * This class represents the Extra Token Power.
 */
public class ExtraTokenPower extends Power {

  public ExtraTokenPower(PlayerInGame playerInfo) {
    super(PowerEffect.EXTRA_TOKEN, playerInfo);
  }

  @Override
  boolean validityCheck(PlayerInGame playerInfo) {
    return false;
  }
}
