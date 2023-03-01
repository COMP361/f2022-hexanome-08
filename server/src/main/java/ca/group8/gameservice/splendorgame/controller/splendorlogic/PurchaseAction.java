package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Bank;
import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.OrientBoard;
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
  private int goldCardsRequired;
  private Position cardPosition;

  private final EnumMap<Colour, Integer> tokensToBePaid;

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
   * @param goldCardsRequired number of gold card required
   * @param tokensToBePaid a enum map of tokens to be paid
   */
  public PurchaseAction(Position cardPosition, DevelopmentCard DevelopmentCard,
                        int goldCardsRequired, EnumMap<Colour, Integer> tokensToBePaid) {
    assert cardPosition != null && DevelopmentCard != null && goldCardsRequired >= 0;
    super.type = this.getClass().getSimpleName();
    this.cardPosition = cardPosition;
    this.curCard = DevelopmentCard;
    this.goldCardsRequired = goldCardsRequired;
    this.tokensToBePaid = tokensToBePaid;
  }

  public int getGoldCardsRequired() {
    return goldCardsRequired;
  }


  @Override
  public void execute(TableTop curTableTop, PlayerInGame playerInGame,
                      ActionGenerator actionGenerator,
                      ActionInterpreter actionInterpreter) {

    List<CardEffect> cardEffects = curCard.getPurchaseEffects();
    int effectNum = cardEffects.size();
    Bank curBank = curTableTop.getBank();
    PurchasedHand purchasedHand = playerInGame.getPurchasedHand();
    int points = curCard.getPrestigePoints();

    if (effectNum == 0) {
      // tokens off from hands
      playerInGame.payTokensToBuy(goldCardsRequired, tokensToBePaid);
      // card goes to hand
      purchasedHand.addDevelopmentCard(curCard);
      // points added
      playerInGame.changePrestigePoints(points);
      // tokens go back to bank
      curBank.returnToken(tokensToBePaid);
      // remove card from board
      BaseBoard baseBoard = (BaseBoard) curTableTop.getBoard(Extension.BASE);
      baseBoard.removeCard(cardPosition);
      // fill up the board
      baseBoard.update();
    }

    if (effectNum == 1) {
      CardEffect curEffect = cardEffects.get(0);
      //String playerName = playerInGame.getName();
      if (curEffect.equals(CardEffect.BURN_CARD)) {
        actionInterpreter.setStashedCard(curCard);
        EnumMap<Colour,Integer> priceOfBurnCard = curCard.getPrice();
        actionInterpreter.setBurnCardInfo(priceOfBurnCard);
        actionGenerator.updateCascadeActions(playerInGame, curCard, curEffect);
      } else {
        // FREE, SATCHEL, RESERVE_NOBLE, DOUBLE_GOLD in here
        playerInGame.payTokensToBuy(goldCardsRequired, tokensToBePaid);
        // tokens go back to bank
        curBank.returnToken(tokensToBePaid);
        // remove card from board
        OrientBoard orientBoard = (OrientBoard) curTableTop.getBoard(Extension.ORIENT);
        orientBoard.removeCard(cardPosition);
        // fill up the board
        orientBoard.update();

        if (curEffect.equals(CardEffect.SATCHEL)) {
          actionInterpreter.setStashedCard(curCard);
        } else {
          // FREE, RESERVE_NOBLE, DOUBLE_GOLD in here
          // card goes to hand
          purchasedHand.addDevelopmentCard(curCard);
          // points added
          playerInGame.changePrestigePoints(points);
        }
        // only update action map if it's not double gold
        if (!curEffect.equals(CardEffect.DOUBLE_GOLD)) {
          actionGenerator.updateCascadeActions(playerInGame, curCard, curEffect);
        }
      }

    }

    // ONLY FOR SATCHEL + FREE
    if (effectNum == 2) {
      actionInterpreter.setStashedCard(curCard);
      int cardLevel = curCard.getLevel();
      actionInterpreter.setFreeCardLevel(cardLevel - 1);
      playerInGame.payTokensToBuy(goldCardsRequired, tokensToBePaid);
      // tokens go back to bank
      curBank.returnToken(tokensToBePaid);
      // remove card from board
      OrientBoard orientBoard = (OrientBoard) curTableTop.getBoard(Extension.ORIENT);
      orientBoard.removeCard(cardPosition);
      // fill up the board
      orientBoard.update();
      actionGenerator.updateCascadeActions(playerInGame, curCard, CardEffect.SATCHEL);
    }

  }

  public EnumMap<Colour, Integer> getTokensToBePaid() {
    return tokensToBePaid;
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
