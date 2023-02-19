package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;


/**
 * Action that allows you to purchase a card.
 */
public class PurchaseAction extends Action {

  private Card curCard;
  private int goldTokenRequired;
  private Position cardPosition;

  @Override
  public Position getCardPosition() {
    if (cardPosition == null) {throw new NullPointerException("Card Position is empty.");}
    return cardPosition;
  }

  @Override
  public Card getCurCard() {
      if (curCard == null) {throw new NullPointerException("Current Card is empty.");}
    return curCard;
  }

  public void setCardPosition(Position cardPosition) {
    this.cardPosition = cardPosition;
  }

  public void setCard(DevelopmentCard card) {
    this.curCard = card;
  }

  /**
   * Constructor of purchase action.
   *
   * @param cardPosition position on board
   * @param card card associated with action
   * @param goldTokenRequired number of gold token required
   */
  public PurchaseAction(Position cardPosition, DevelopmentCard card, int goldTokenRequired) {
    assert cardPosition != null && card != null && goldTokenRequired>=0;
    this.cardPosition = cardPosition;
    this.curCard = card;
    this.goldTokenRequired = goldTokenRequired;
  }

  public int getGoldTokenRequired() {
    return goldTokenRequired;
  }

  public void setGoldTokenRequired(int goldTokenRequired) {
    this.goldTokenRequired = goldTokenRequired;
  }


  @Override
  public void execute(TableTop curTableTop, PlayerInGame playerInGame,
                      SplendorActionListGenerator actionListGenerator,
                      SplendorActionInterpreter actionInterpreter) {

  }

  /*
  public void execute(GameInfo currentGameState, PlayerInGame playerState) {
    // TODO: For now, just do a simple downcast assuming the card is DevelopmentCard
    Card card = this.curCard;
    PurchasedHand hand = playerState.getPurchasedHand();
    TokenHand tokenHand = playerState.getTokenHand();
    EnumMap<Colour, Integer> playerGems = playerState.getTotalGems();
    // update player's prestige points
    int cardPoints = card.getPrestigePoints();
    int playerCurPoints = playerState.getPrestigePoints();

    //Changed this from previous version --> now we just add points VS. using set method.
    playerState.addPrestigePoints(cardPoints);

    // update player's purchase hand
    for (Colour colour : Colour.values()) {
      if (colour.equals(Colour.GOLD)) {
        if (goldTokenRequired > 0) {
          int currentGoldTokenHeld = tokenHand.getAllTokens().get(colour);
          tokenHand.getAllTokens().put(colour, currentGoldTokenHeld - goldTokenRequired);
        }
        continue;
      }
      int discountedPrice = card.getPrice().get(colour) - playerGems.get(colour);
      if (discountedPrice > 0) {
        int remainingTokens = tokenHand.getAllTokens().get(colour) - discountedPrice;
        tokenHand.getAllTokens().put(colour, Math.max(remainingTokens, 0));
      }
    }
    hand.addDevelopmentCard(card);
    int level = card.getLevel();
    // replace the card with another new card draw from deck
    DevelopmentCard newCard =
        currentGameState.getTableTop().getBaseBoard().getBaseDecks().get(level).remove(0);
    currentGameState.getTableTop().getBaseBoard().replaceCardOnBoard(cardPosition, newCard);
  }

   */


  @Override
  public boolean checkIsCardAction() {
    return true;
  }
}
