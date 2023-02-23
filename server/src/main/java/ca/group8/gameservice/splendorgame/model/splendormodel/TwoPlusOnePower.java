package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * This class represents the TwoPlusOnePower.
 */
public class TwoPlusOnePower extends Power {

  public TwoPlusOnePower() {
    super(PowerEffect.TWO_PLUS_ONE);
  }

  @Override
  boolean validityCheck(PlayerInGame playerInfo) {
    //Must have 2 white gem cards
    return playerInfo.getPurchasedHand().getGemCountOfColour(Colour.WHITE) >= 2;
  }
}
