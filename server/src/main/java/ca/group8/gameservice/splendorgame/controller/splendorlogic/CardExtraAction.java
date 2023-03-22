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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

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
      burnActionHelper( playerInGame, actionInterpreter);
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

  public CardEffect getCardEffect() {
    return cardEffect;
  }

  public Position getPosition() {
    return position;
  }

  /*
  //TODO: Ensure this method works once merged with code for CardEffect enum & new DevCard class.
  public CardEffect getCardEffect() {
    return curCard.getPurchaseEffect();
  }
   */

  /**
   * Helper for reserve action.
   *
   * @param curTableTop curTableTop
   * @param curPlayer curPlayer
   */
  public void reserveNobleActionHelper(TableTop curTableTop,
                                       PlayerInGame curPlayer,
                                       ActionGenerator actionGenerator) {

    //// Make sure curCard is right type
    //if (!(this.curCard instanceof NobleCard)) {
    //  //throw new SplendorGameException("Error: Reserve Card is not a NobleCard");
    //  System.out.println("ReserveNoble: Not a noble");
    //  return;
    //}
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
    ownedCard.pairCard(satchel);

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




    /*
    //which row and column
    int index = position.getY();
    int level = satchel.getLevel();

    //update the board with new card
    //took it out, done in PurchaseAction

    OrientBoard board = ((OrientBoard) curTableTop.getBoard(Extension.ORIENT));
    Card replacement = board.popLevelCardFromDeck(level);
    board.getLevelCardsOnBoard(level)[index] = (DevelopmentCard) replacement;
     */
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
    int effectNum = cardEffects.size();
    int prestigePoints = freeCard.getPrestigePoints();

    //if it is a base card
    if (effectNum == 0) {
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
      CardEffect currentEffect = cardEffects.get(0);

      OrientBoard orientBoard = (OrientBoard) curTableTop.getBoard(Extension.ORIENT);
      //remove freeCard from Board, replace.
      orientBoard.removeCard(this.position);
      orientBoard.update();

      if (currentEffect == CardEffect.SATCHEL) {
        // stash the SATCHEL since we this card can not just go to player's purchase
        associatedActionInterpreter.setStashedCard(freeCard);
      } else {
        // otherwise: FREE OR DOUBLE_GOLD OR RESERVE_NOBLE can just go to player's hand
        curPlayer.getPurchasedHand().addDevelopmentCard(freeCard);
        curPlayer.changePrestigePoints(prestigePoints);
      }
      //TODO: Why do we leave the BURN out in here? -- for julia and young to fix
      // i have no clue how to do this              ----- by ruoyu ;)
      if (currentEffect != CardEffect.BURN_CARD) {
        actionGenerator.updateCascadeActions(curPlayer, freeCard, currentEffect);
      }


      //// Case for most orient cards
      //if (effectNum == 1) {
      //  OrientBoard orientBoard = (OrientBoard) curTableTop.getBoard(Extension.ORIENT);
      //  //remove freeCard from Board, replace.
      //  orientBoard.removeCard(this.position);
      //  orientBoard.update();
      //
      //  if (currentEffect == CardEffect.SATCHEL) {
      //    // stash the SATCHEL since we this card can not just go to player's purchase
      //    associatedActionInterpreter.setStashedCard(freeCard);
      //  } else {
      //    // otherwise: FREE OR DOUBLE_GOLD OR RESERVE_NOBLE can just go to player's hand
      //    curPlayer.getPurchasedHand().addDevelopmentCard(freeCard);
      //    curPlayer.changePrestigePoints(prestigePoints);
      //  }
      //  //TODO: Why do we leave the BURN out in here? -- for julia and young to fix
      //  // i have no clue how to do this              ----- by ruoyu ;)
      //  if (currentEffect != CardEffect.BURN_CARD) {
      //    actionGenerator.updateCascadeActions(curPlayer, freeCard, currentEffect);
      //  }
      //
      //  // Special case where an orient card has satchel and free card.
      //} else if (effectNum == 2) {
      //  associatedActionInterpreter.setStashedCard(freeCard);
      //  associatedActionInterpreter.setFreeCardLevel(freeCard.getLevel() - 1);
      //
      //  actionGenerator.updateCascadeActions(curPlayer, freeCard, CardEffect.SATCHEL);
      //
      //  //Should never reach here.
      //} else {
      //  System.out.println("FreeCard: Error, size of CardEffect List is not 0,1,2");
      //}
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
      logger.info("Should be no gems left to burn: " +(burnNumber - gemNumber) );
      //take stashed card and add to player's hand
      newCard = associatedActionInterpreter.getStashedCard();
      curPlayer.getPurchasedHand().addDevelopmentCard(newCard);

      //add prestige points
      int newPrestigePoints = newCard.getPrestigePoints();
      curPlayer.changePrestigePoints(newPrestigePoints);

      associatedActionInterpreter.setStashedCard(null);
      actionGenerator.getPlayerActionMaps().put(curPlayer.getName(), new HashMap<>());
    } else {
      logger.info("Gems still Left to burn: " +(burnNumber - gemNumber) );
      // the curCard in CardExtraAction refer to the card that IS to be burnt
      // not the card that we purchased by burning
      actionGenerator.updateCascadeActions(curPlayer, newCard, CardEffect.BURN_CARD);
    }
  }

}
