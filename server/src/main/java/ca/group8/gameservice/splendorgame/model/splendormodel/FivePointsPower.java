package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * This class represents the Five extra prestige points power.
 */
public class FivePointsPower extends Power {

  public FivePointsPower() {
    super(PowerEffect.FIVE_POINTS);
    super.type = this.getClass().getSimpleName();
  }

  @Override
  public boolean validityCheck(PlayerInGame playerInfo) {
    //must have 5 green gem cards and 1 noble
    return playerInfo.getPurchasedHand().getGemCountOfColour(Colour.GREEN) >= 5
        && playerInfo.getPurchasedHand().getNobleCards().size() >= 1;
  }
}
