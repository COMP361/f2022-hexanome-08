package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.BaseBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.CardEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.NobleCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.OrientBoard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an extra DevelopmentCard action.
 */
public class CardExtraAction extends Action {

  private final CardEffect cardEffect;
  private final Position position;
  private final Card curCard;

  /**
   * Constructor.
   *
   * @param curCard    DevelopmentCard which "unlocks" this extra action
   * @param cardEffect The type of extra action
   */
  public CardExtraAction(Card curCard, CardEffect cardEffect, Position position) {
    assert curCard != null;
    super.type = this.getClass().getSimpleName();
    this.curCard = curCard;
    this.cardEffect = cardEffect;
    this.position = position;
  }


  @Override
  public void execute(TableTop curTableTop, PlayerInGame playerInGame,
                      ActionGenerator actionGenerator, ActionInterpreter actionInterpreter) {
    //based on the cardEffect, execute the associated helper
    if (this.cardEffect == CardEffect.BURN_CARD) {
      burnActionHelper(playerInGame, actionInterpreter);
    } else if (this.cardEffect == CardEffect.SATCHEL) {
      satchelActionHelper(playerInGame, actionInterpreter);
    } else if (this.cardEffect == CardEffect.RESERVE_NOBLE) {
      reserveNobleActionHelper(curTableTop, playerInGame, actionGenerator);
    } else if (this.cardEffect == CardEffect.FREE_CARD) {
      //Don't know which card is the freeCard
      freeCardActionHelper(curTableTop, playerInGame, actionInterpreter);
    }
  }

  public Card getCurCard() {
    return curCard;
  }

  public Position getCardPosition() {
    return position;
  }

  public Position getPosition() {
    return position;
  }

  /**
   * Helper for reserve action.
   *
   * @param curTableTop curTableTop
   * @param curPlayer curPlayer
   */
  public void reserveNobleActionHelper(TableTop curTableTop,
                                       PlayerInGame curPlayer,
                                       ActionGenerator actionGenerator) {
    NobleCard noble = (NobleCard) this.curCard;
    //remove noble from base board
    ((BaseBoard) curTableTop.getBoard(Extension.BASE)).removeNoble(noble);
    //add it to player's reserve hand
    curPlayer.getReservedHand().addNobleCard(noble);

    // clear the player's action to empty to indicate end of turn
    actionGenerator.getPlayerActionMaps().put(curPlayer.getName(), new HashMap<>());
  }

  /**
   * Helper to satchelAction.
   *
   * @param curPlayer curPlayer
   * @param associatedActionInterpreter associatedActionInterpreter
   */
  public void satchelActionHelper(PlayerInGame curPlayer,
                                  ActionInterpreter associatedActionInterpreter) {

    if (!(this.curCard instanceof DevelopmentCard)) {
      //throw new SplendorGameException("Error: Reserve Card is not a NobleCard");
      System.out.println("Satchel: Not a DevelopmentCard");
      return;
    }
    //owned card = card you are pairing to. satchel is card you just bought.
    DevelopmentCard ownedCard = (DevelopmentCard) this.curCard;
    DevelopmentCard satchel = associatedActionInterpreter.getStashedCard();

    //pair the card
    //ownedCard.setIsPaired(true);
    //ownedCard.setPairedCard(satchel);

    Logger logger = LoggerFactory.getLogger(CardExtraAction.class);
    logger.warn("Pairing state of card of the action (before): " + ownedCard.isPaired());
    ownedCard.pairCard(satchel);
    logger.warn("Pairing state of card of the action (after): " + ownedCard.isPaired());
    for (DevelopmentCard card : curPlayer.getPurchasedHand().getDevelopmentCards()) {
      if (card.equals(ownedCard)) {
        logger.warn("The card in hand pairing state: " + card.isPaired());
      }
    }

    //add card to hand, add prestige points
    int prestigeAmount = satchel.getPrestigePoints();
    curPlayer.getPurchasedHand().addDevelopmentCard(satchel);
    curPlayer.changePrestigePoints(prestigeAmount);
    ActionGenerator actionGenerator = associatedActionInterpreter.getActionGenerator();

    // this is a regular satchel -> reset stash to null is ok
    if (satchel.getPurchaseEffects().size() == 1) {
      //reset stashedCard to null
      associatedActionInterpreter.setStashedCard(null);
      actionGenerator.getPlayerActionMaps().put(curPlayer.getName(), new HashMap<>());
    }

    // this is a regular satchel + free -> DO NOT reset stash to null
    // instead, update cascade with free actions
    if (satchel.getPurchaseEffects().size() == 2) {
      actionGenerator.updateCascadeActions(curPlayer, satchel, CardEffect.FREE_CARD);

    }

  }

  /**
   * Helper for free card action.
   *
   * @param curTableTop curTableTop
   * @param curPlayer curPlayer
   * @param associatedActionInterpreter associatedActionInterpreter
   */
  //TODO
  public void freeCardActionHelper(TableTop curTableTop,
                                   PlayerInGame curPlayer,
                                   ActionInterpreter associatedActionInterpreter) {

    DevelopmentCard freeCard = (DevelopmentCard) this.curCard;
    List<CardEffect> cardEffects = freeCard.getPurchaseEffects();
    int prestigePoints = freeCard.getPrestigePoints();

    Logger logger = LoggerFactory.getLogger(CardExtraAction.class);
    logger.warn("Cur card colour: " + freeCard.getGemColour());
    logger.warn("Cur card is base: " + freeCard.isBaseCard());

    //if it is a base card
    if (freeCard.isBaseCard()) {
      BaseBoard baseBoard = (BaseBoard) curTableTop.getBoard(Extension.BASE);
      //remove freeCard from Board, replace
      baseBoard.removeCard(this.position);
      baseBoard.update();
      curPlayer.getPurchasedHand().addDevelopmentCard(freeCard);
      curPlayer.changePrestigePoints(prestigePoints);
      // reset the player action, so we know this free is over
      associatedActionInterpreter.getActionGenerator()
          .getPlayerActionMaps().put(curPlayer.getName(), new HashMap<>());
      // set free level to 0 to avoid infinite loop
      associatedActionInterpreter.setFreeCardLevel(0);

      //if it is an orient card
    } else {
      ActionGenerator actionGenerator = associatedActionInterpreter.getActionGenerator();
      OrientBoard orientBoard = (OrientBoard) curTableTop.getBoard(Extension.ORIENT);
      //remove freeCard from Board, replace.
      orientBoard.removeCard(this.position);
      orientBoard.update();

      // might contain SATCHEL / FREE / RESERVE_NOBLE
      if (cardEffects.contains(CardEffect.SATCHEL)) {
        // stash the SATCHEL since we this card can not just go to player's purchase
        associatedActionInterpreter.setStashedCard(freeCard);
      } else {
        // otherwise: FREE OR RESERVE_NOBLE can just go to player's hand
        curPlayer.getPurchasedHand().addDevelopmentCard(freeCard);
        curPlayer.changePrestigePoints(prestigePoints);
      }


      // if the orient has no effects OR double gold, then guarantee clear action map
      if (cardEffects.isEmpty() || cardEffects.contains(CardEffect.DOUBLE_GOLD)) {
        associatedActionInterpreter.getActionGenerator()
            .getPlayerActionMaps().put(curPlayer.getName(), new HashMap<>());
      } else {
        CardEffect currentEffect = cardEffects.get(0);
        actionGenerator.updateCascadeActions(curPlayer, freeCard, currentEffect);
      }
      //always set to 0 because no matter what the cascading action is, it is not a free card.
      // set free level to 0 to avoid infinite loop
      associatedActionInterpreter.setFreeCardLevel(0);

    }
  }

  /**
   * Helper for burnAction.
   *
   * @param curPlayer curPlayer
   * @param associatedActionInterpreter associatedActionInterpreter
   */
  public void burnActionHelper(PlayerInGame curPlayer,
                               ActionInterpreter associatedActionInterpreter) {

    DevelopmentCard cardToBurn = (DevelopmentCard) this.curCard;
    final int burnNumber = associatedActionInterpreter.getBurnCardCount();
    int gemNumber = cardToBurn.getGemNumber();
    //make it negative since you're taking away points
    int prestigePoints = -1 * cardToBurn.getPrestigePoints();

    //remove burned card from purchaseHand and remove it's prestige points
    curPlayer.getPurchasedHand().removeDevelopmentCard((DevelopmentCard) this.curCard);
    curPlayer.changePrestigePoints(prestigePoints);

    //remove the gems from burned card from total amount needed to burn
    associatedActionInterpreter.removeBurnCardCount(gemNumber);
    ActionGenerator actionGenerator = associatedActionInterpreter.getActionGenerator();
    Logger logger = LoggerFactory.getLogger(ActionInterpreter.class);

    //if you have finished burning cards
    //TODO: NOTE, this new card is the one we want to (potentially)
    // continue generate burn action on, not curCard!
    DevelopmentCard newCard = associatedActionInterpreter.getStashedCard();
    if (burnNumber - gemNumber <= 0) {
      logger.info("Should be no gems left to burn: " + (burnNumber - gemNumber));
      //take stashed card and add to player's hand
      newCard = associatedActionInterpreter.getStashedCard();
      curPlayer.getPurchasedHand().addDevelopmentCard(newCard);

      //add prestige points
      int newPrestigePoints = newCard.getPrestigePoints();
      curPlayer.changePrestigePoints(newPrestigePoints);

      associatedActionInterpreter.setStashedCard(null);
      actionGenerator.getPlayerActionMaps().put(curPlayer.getName(), new HashMap<>());
    } else {
      logger.info("Gems still Left to burn: " + (burnNumber - gemNumber));
      // the curCard in CardExtraAction refer to the card that IS to be burnt
      // not the card that we purchased by burning
      actionGenerator.updateCascadeActions(curPlayer, newCard, CardEffect.BURN_CARD);
    }
  }

}
