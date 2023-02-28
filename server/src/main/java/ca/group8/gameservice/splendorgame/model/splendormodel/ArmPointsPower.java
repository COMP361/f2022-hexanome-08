package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.List;

/**
 * This class represents the Arm Points Power.
 */
public class ArmPointsPower extends Power {

  public ArmPointsPower() {
    super(PowerEffect.ARM_POINTS);
    super.type = this.getClass().getSimpleName();
  }

  @Override
  public boolean validityCheck(PlayerInGame playerInfo) {
    //Must have 3 black gem cards for this to be valid
    return playerInfo.getPurchasedHand().getGemCountOfColour(Colour.BLACK) >= 3;
  }
}
