package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Bank;
import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.PurchasedHand;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import java.util.EnumMap;
import java.util.List;


/**
 * Action that allows you to purchase a DevelopmentCard.
 */
public class PurchaseAction extends Action {

  private DevelopmentCard curCard;
  private int goldTokenRequired;
  private Position cardPosition;

  @Override
  public Position getCardPosition() {
    if (cardPosition == null) {
      throw new NullPointerException("DevelopmentCard Position is empty.");
    }
    return cardPosition;
  }

  @Override
  public DevelopmentCard getCurCard() {
    if (curCard == null) {
      throw new NullPointerException("Current DevelopmentCard is empty.");
    }
    return curCard;
  }

  public void setCardPosition(Position cardPosition) {
    this.cardPosition = cardPosition;
  }

  public void setCard(DevelopmentCard DevelopmentCard) {
    this.curCard = DevelopmentCard;
  }

  /**
   * Constructor of purchase action.
   *
   * @param cardPosition      position on board
   * @param DevelopmentCard   DevelopmentCard associated with action
   * @param goldTokenRequired number of gold token required
   */
  public PurchaseAction(Position cardPosition, DevelopmentCard DevelopmentCard,
                        int goldTokenRequired) {
    assert cardPosition != null && DevelopmentCard != null && goldTokenRequired >= 0;
    super.type = this.getClass().getSimpleName();
    this.cardPosition = cardPosition;
    this.curCard = DevelopmentCard;
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
                      ActionGenerator actionListGenerator,
                      ActionInterpreter actionInterpreter) {

    //TODO: Double check that we want this assertion statement
    assert curCard instanceof DevelopmentCard;

    //cast DevelopmentCard to Development DevelopmentCard
    DevelopmentCard curCard = (DevelopmentCard) this.curCard;

    List<CardEffect> cardEffects = curCard.getPurchaseEffects();
    int effectNum = cardEffects.size();
    Bank curBank = curTableTop.getBank();
    PurchasedHand purchasedHand = playerInGame.getPurchasedHand();
    int points = curCard.getPrestigePoints();

    if (effectNum == 0) {
      EnumMap<Colour, Integer> tokensSpent =
          playerInGame.payTokensToBuy(goldTokenRequired, curCard);
      purchasedHand.addDevelopmentCard(curCard);
      playerInGame.addPrestigePoints(points);
      curBank.returnToken(tokensSpent);

      BaseBoard baseBoard = (BaseBoard) curTableTop.getBoard(Extension.BASE);
      baseBoard.removeCard(cardPosition);
    }

  }

  /*
  public void execute(GameInfo currentGameState, PlayerInGame playerState) {
    // TODO: For now, just do a simple downcast assuming the DevelopmentCard is DevelopmentCard
    DevelopmentCard DevelopmentCard = this.curCard;
    PurchasedHand hand = playerState.getPurchasedHand();
    TokenHand tokenHand = playerState.getTokenHand();
    EnumMap<Colour, Integer> playerGems = playerState.getTotalGems();
    // update player's prestige points
    int cardPoints = DevelopmentCard.getPrestigePoints();
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
      int discountedPrice = DevelopmentCard.getPrice().get(colour) - playerGems.get(colour);
      if (discountedPrice > 0) {
        int remainingTokens = tokenHand.getAllTokens().get(colour) - discountedPrice;
        tokenHand.getAllTokens().put(colour, Math.max(remainingTokens, 0));
      }
    }
    hand.addDevelopmentCard(DevelopmentCard);
    int level = DevelopmentCard.getLevel();
    // replace the DevelopmentCard with another new DevelopmentCard draw from deck
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
