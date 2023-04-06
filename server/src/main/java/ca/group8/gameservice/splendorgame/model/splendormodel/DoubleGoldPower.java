package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * This class represents the Double Gold Power.
 */
public class DoubleGoldPower extends Power {

  /**
   * Constructor.
   */
  public DoubleGoldPower() {
    super(PowerEffect.DOUBLE_GOLD);
    super.type = this.getClass().getSimpleName();
  }

  @Override
  public boolean validityCheck(PlayerInGame playerInfo) {
    //Must have 3 blue gem cards, and 1 black gem card
    PurchasedHand playerHand = playerInfo.getPurchasedHand();
    return playerHand.getGemCountOfColour(Colour.BLUE) >= 3
        && playerHand.getGemCountOfColour(Colour.BLACK) >= 1;
  }

}
