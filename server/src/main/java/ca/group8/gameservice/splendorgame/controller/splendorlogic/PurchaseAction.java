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
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Action that allows you to purchase a DevelopmentCard.
 */
public class PurchaseAction extends Action {

  private final EnumMap<Colour, Integer> tokensToBePaid;
  private final DevelopmentCard curCard;
  private final int goldCardsRequired;
  private final Position cardPosition;


  /**
   * Constructor of purchase action.
   *
   * @param cardPosition      position on board
   * @param developmentCard   DevelopmentCard associated with action
   * @param goldCardsRequired number of gold card required
   * @param tokensToBePaid    a enum map of tokens to be paid
   */
  public PurchaseAction(Position cardPosition, DevelopmentCard developmentCard,
                        int goldCardsRequired, EnumMap<Colour, Integer> tokensToBePaid) {
    assert cardPosition != null && developmentCard != null && goldCardsRequired >= 0;
    super.type = this.getClass().getSimpleName();
    this.cardPosition = cardPosition;
    this.curCard = developmentCard;
    this.goldCardsRequired = goldCardsRequired;
    this.tokensToBePaid = tokensToBePaid;
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

      //if cardPosition.getX()==0, it would mean this is from reservedHand. Therefore
      //we would skip the steps to remove & replace card on board.
      if (cardPosition.getX() != 0) {
        if (curCard.isBaseCard()) {
          // remove card from board
          BaseBoard baseBoard = (BaseBoard) curTableTop.getBoard(Extension.BASE);
          baseBoard.removeCard(cardPosition);
          // fill up the board
          baseBoard.update();
        } else {
          // remove card from board
          OrientBoard orientBoard = (OrientBoard) curTableTop.getBoard(Extension.ORIENT);
          orientBoard.removeCard(cardPosition);
          // fill up the board
          orientBoard.update();
        }
      } else { //means this is a reserved card, so remove from player's ReserveHand
        playerInGame.getReservedHand().removeDevelopmentCard(curCard);
      }
      // no possible chance of generating any cascade in here, return and reset current
      // player action map
      actionGenerator.getPlayerActionMaps().put(playerInGame.getName(), new HashMap<>());
      return;
    }

    if (effectNum == 1) {
      CardEffect curEffect = cardEffects.get(0);
      //String playerName = playerInGame.getName();
      if (curEffect.equals(CardEffect.BURN_CARD)) {
        Logger logger = LoggerFactory.getLogger(ActionInterpreter.class);
        actionInterpreter.setStashedCard(curCard);
        EnumMap<Colour, Integer> priceOfBurnCard = curCard.getPrice();
        if (cardPosition.getX() != 0) {
          // remove card from board
          OrientBoard orientBoard = (OrientBoard) curTableTop.getBoard(Extension.ORIENT);
          orientBoard.removeCard(cardPosition);
          // fill up the board
          orientBoard.update();
        } else { //means this is a reserved card, so remove from player's ReserveHand
          playerInGame.getReservedHand().removeDevelopmentCard(curCard);
        }
        actionInterpreter.setBurnCardInfo(priceOfBurnCard);
        actionGenerator.updateCascadeActions(playerInGame, curCard, curEffect);
      } else {
        // FREE, SATCHEL, RESERVE_NOBLE, DOUBLE_GOLD in here
        playerInGame.payTokensToBuy(goldCardsRequired, tokensToBePaid);
        // tokens go back to bank
        curBank.returnToken(tokensToBePaid);

        //if cardPosition.getX()==0, it would mean this is from reservedHand. Therefore
        //we would skip the steps to remove & replace card on board.
        if (cardPosition.getX() != 0) {

          // remove card from board
          OrientBoard orientBoard = (OrientBoard) curTableTop.getBoard(Extension.ORIENT);
          orientBoard.removeCard(cardPosition);
          // fill up the board
          orientBoard.update();

        } else { //means this is a reserved card, so remove from player's ReserveHand
          playerInGame.getReservedHand().removeDevelopmentCard(curCard);

        }

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
        } else {
          // the current effect is indeed double gold, no cascade possible, reset map
          actionGenerator.getPlayerActionMaps().put(playerInGame.getName(), new HashMap<>());
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

      //if cardPosition.getX()==0, it would mean this is from reservedHand. Therefore
      //we would skip the steps to remove & replace card on board.
      //if cardPosition.getX()==0, it would mean this is from reservedHand. Therefore
      //we would skip the steps to remove & replace card on board.
      if (cardPosition.getX() != 0) {

        // remove card from board
        OrientBoard orientBoard = (OrientBoard) curTableTop.getBoard(Extension.ORIENT);
        orientBoard.removeCard(cardPosition);
        // fill up the board
        orientBoard.update();

      } else { //means this is a reserved card, so remove from player's ReserveHand
        playerInGame.getReservedHand().removeDevelopmentCard(curCard);

      }
      // always have cascade, there is no situation where you purchased a new 2 effect card
      // but no cascade is happening
      actionGenerator.updateCascadeActions(playerInGame, curCard, CardEffect.SATCHEL);
    }


  }

  public EnumMap<Colour, Integer> getTokensToBePaid() {
    return tokensToBePaid;
  }


  public Position getCardPosition() {
    return cardPosition;
  }

  public DevelopmentCard getCurCard() {
    return curCard;
  }
}
