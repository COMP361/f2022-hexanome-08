package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * This class represents the Double Gold Power.
 */
public class DoubleGoldPower extends Power {

  public DoubleGoldPower() {
    super(PowerEffect.DOUBLE_GOLD);
  }

  @Override
  boolean validityCheck(PlayerInGame playerInfo) {
    return false;
  }
}
