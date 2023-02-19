package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * This class represents the Arm Points Power.
 */
public class ArmPointsPower extends Power {

  public ArmPointsPower(PlayerInGame playerInfo) {
    super(PowerEffect.ARM_POINTS, playerInfo);
  }

  @Override
  boolean validityCheck(PlayerInGame playerInfo) {
    return false;
  }
}
