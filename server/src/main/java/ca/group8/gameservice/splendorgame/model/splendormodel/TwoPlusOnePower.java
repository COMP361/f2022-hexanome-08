package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * This class represents the TwoPlusOnePower.
 */
public class TwoPlusOnePower extends Power {

  /**
   * Constructor.
   */
  public TwoPlusOnePower() {
    super(PowerEffect.TWO_PLUS_ONE);
    super.type = this.getClass().getSimpleName();
  }

  @Override
  public boolean validityCheck(PlayerInGame playerInfo) {
    //Must have 2 white gem cards
    return playerInfo.getPurchasedHand().getGemCountOfColour(Colour.WHITE) >= 2;
  }
}
