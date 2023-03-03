package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.*;

/**
 * This class represents an extra DevelopmentCard action.
 */
public class CardExtraAction extends Action {

  private Card curCard;
  //TODO: Check this works after code is merged
  private final CardEffect cardEffect;

  private final Position position;

  //TODO: Check this works after code is merged

  /**
   * Constructor.
   *
   * @param curCard    DevelopmentCard which "unlocks" this extra action
   * @param cardEffect The type of extra action
   */
  public CardExtraAction(Card curCard, CardEffect cardEffect,
                         Position position) {
    assert curCard != null;
    super.type = this.getClass().getSimpleName();
    curCard = curCard;
    this.cardEffect = cardEffect;
    this.position = position;
  }


  @Override
  public void execute(TableTop curTableTop, PlayerInGame playerInGame,
                      ActionGenerator actionListGenerator, ActionInterpreter actionInterpreter) {
    //based on the cardEffect, execute the associated helper
    if(this.cardEffect == cardEffect.BURN_CARD){
      burnActionHelper(curTableTop, playerInGame, actionListGenerator, actionInterpreter);
    }else if(this.cardEffect == cardEffect.SATCHEL){
      satchelActionHelper(curTableTop, playerInGame, actionListGenerator, actionInterpreter);
    }else if(this.cardEffect == cardEffect.RESERVE_NOBLE){
      reserveNobleActionHelper(curTableTop, playerInGame, actionListGenerator);
    }else if(this.cardEffect == cardEffect.FREE_CARD){
      //Don't know which card is the freeCard
      freeCardActionHelper(curTableTop, playerInGame, actionInterpreter);
    }
  }

  @Override
  public boolean checkIsExtraAction() {
    return false;
  }

  @Override
  public boolean checkIsCardAction() {
    return false;
  }

  @Override
  public Card getCurCard() {
    return curCard;
  }

  @Override
  //TODO: Do we have an associated position? If not, have it throw a null pointer exception.
  public Position getCardPosition() {
    return null;
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

  public void reserveNobleActionHelper(TableTop curTableTop,
                                       PlayerInGame curPlayer,
                                       ActionGenerator associatedActionGenerator) {

    // Make sure curCard is right type
    if (!(this.curCard instanceof NobleCard)) {
      //throw new SplendorGameException("Error: Reserve Card is not a NobleCard");
      System.out.println("ReserveNoble: Not a noble");
      return;
    }
    NobleCard noble = (NobleCard) this.curCard;
    //remove noble from base board
    ((BaseBoard) curTableTop.getBoard(Extension.BASE)).removeNoble(noble);
    //add it to player's reserve hand
    curPlayer.getReservedHand().addNobleCard(noble);
  }

  public void satchelActionHelper(TableTop curTableTop,
                                  PlayerInGame curPlayer,
                                  ActionGenerator associatedActionGenerator,
                                  ActionInterpreter associatedActionInterpreter) {

    if (!(this.curCard instanceof DevelopmentCard)) {
      //throw new SplendorGameException("Error: Reserve Card is not a NobleCard");
      System.out.println("Satchel: Not a DevelopmentCard");
      return;
    }
    //owned card = card you are pairing to. satchel is card you just bought.
    DevelopmentCard ownedCard = (DevelopmentCard) this.curCard;
    DevelopmentCard satchel = (DevelopmentCard) associatedActionInterpreter.getStashedCard();

    //pair the card
    ownedCard.setIsPaired(true);
    ownedCard.setPairedCard(satchel);

    //add card to hand, add prestige points
    int prestigeAmount = satchel.getPrestigePoints();
    curPlayer.getPurchasedHand().addDevelopmentCard(satchel);
    curPlayer.changePrestigePoints(prestigeAmount);

    //reset stashedCard to null
    associatedActionInterpreter.setStashedCard(null);

    //which row and column
    int index = position.getY();
    int level = satchel.getLevel();

    //update the board with new card
    OrientBoard board = ((OrientBoard) curTableTop.getBoard(Extension.ORIENT));
    Card replacement = board.popLevelCardFromDeck(level);
    board.getLevelCardsOnBoard(level)[index] = (DevelopmentCard) replacement;
  }

  //TODO
  public void freeCardActionHelper(TableTop curTableTop,
                                   PlayerInGame curPlayer,
                                   ActionInterpreter associatedActionInterpreter) {

  }

  public void burnActionHelper(TableTop curTableTop,
                               PlayerInGame curPlayer,
                               ActionGenerator associatedActionGenerator,
                               ActionInterpreter associatedActionInterpreter) {

    DevelopmentCard cardToBurn = (DevelopmentCard) this.curCard;
    int burnNumber = associatedActionInterpreter.getBurnCardCount();
    int gemNumber = cardToBurn.getGemNumber();
    //make it negative since you're taking away points
    int prestigePoints = -1 * cardToBurn.getPrestigePoints();

    //remove burned card from purchaseHand and remove it's prestige points
    curPlayer.getPurchasedHand().removeDevelopmentCard((DevelopmentCard) this.curCard);
    curPlayer.changePrestigePoints(prestigePoints);

    //remove the gems from burned card from total amount needed to burn
    associatedActionInterpreter.removeBurnCardCount(gemNumber);

    //if you have finished burning cards
    if(burnNumber - gemNumber <= 0){
      //take stashed card and add to player's hand
      DevelopmentCard newCard = associatedActionInterpreter.getStashedCard();
      curPlayer.getPurchasedHand().addDevelopmentCard(newCard);

      //add prestige points
      int newPrestigePoints = newCard.getPrestigePoints();
      curPlayer.changePrestigePoints(newPrestigePoints);

      associatedActionInterpreter.setStashedCard(null);
    }

    //TODO: in the sequence diagram it says the remove the stashed card from board but I think thats done in PurchaseAction
  }

}
