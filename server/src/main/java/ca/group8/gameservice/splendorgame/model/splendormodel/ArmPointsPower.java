package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * This class represents the Arm Points Power.
 */
public class ArmPointsPower extends Power {

  public ArmPointsPower() {
    super(PowerEffect.ARM_POINTS);
  }

  @Override
  boolean validityCheck(PlayerInGame playerInfo) {
    return false;
  }
}
