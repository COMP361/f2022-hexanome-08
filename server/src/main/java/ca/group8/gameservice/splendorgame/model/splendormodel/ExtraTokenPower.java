package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * This class represents the Extra Token Power.
 */
public class ExtraTokenPower extends Power {

  /**
   * Constructor.
   */

  public ExtraTokenPower() {
    super(PowerEffect.EXTRA_TOKEN);
    super.type = this.getClass().getSimpleName();
  }

  @Override
  public boolean validityCheck(PlayerInGame playerInfo) {
    //Must have 3 red gem cards and 1 white gem card
    PurchasedHand purchasedHand = playerInfo.getPurchasedHand();
    return purchasedHand.getGemCountOfColour(Colour.RED) >= 3
        && purchasedHand.getGemCountOfColour(Colour.WHITE) >= 1;
  }
}
