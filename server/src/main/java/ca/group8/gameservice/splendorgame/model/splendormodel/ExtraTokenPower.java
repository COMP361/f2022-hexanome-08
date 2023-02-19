package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * This class represents the Extra Token Power.
 */
public class ExtraTokenPower extends Power {

  public ExtraTokenPower() {
    super(PowerEffect.EXTRA_TOKEN);
  }

  @Override
  boolean validityCheck(PlayerInGame playerInfo) {
    return false;
  }
}
