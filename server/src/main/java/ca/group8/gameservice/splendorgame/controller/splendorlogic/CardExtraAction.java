package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;

/**
 * This class represents an extra card action.
 */
public class CardExtraAction extends Action {

  private Card curCard;
  //TODO: Check this works after code is merged
  private final CardEffect cardEffect;

  //TODO: Check this works after code is merged

  /**
   * Constructor.
   *
   * @param card Card which "unlocks" this extra action
   * @param cardEffect The type of extra action
   */
  public CardExtraAction(Card card, CardEffect cardEffect) {
    assert curCard != null;
    curCard = card;
    this.cardEffect = cardEffect;
  }

  @Override
  public void execute(TableTop curTableTop, PlayerInGame playerInGame,
                      SplendorActionListGenerator actionListGenerator,
                      SplendorActionInterpreter actionInterpreter) {

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

  /*
  //TODO: Ensure this method works once merged with code for CardEffect enum & new DevCard class.
  public CardEffect getCardEffect() {
    return curCard.getPurchaseEffect();
  }
   */

  //TODO:
  public void reserveNobleActionHelper(TableTop curTableTop,
                                       PlayerInGame curPlayer,
                                       SplendorActionListGenerator associatedActionGenerator) {

  }

  //TODO
  public void satchelActionHelper(TableTop curTableTop,
                                  PlayerInGame curPlayer,
                                  SplendorActionListGenerator associatedActionGenerator) {

  }

  //TODO
  public void freeCardActionHelper(DevelopmentCard freeCard,
                                   TableTop curTableTop,
                                   PlayerInGame curPlayer,
                                   SplendorActionInterpreter associatedActionInterpreter) {

  }

  //TODO
  public void burnActionHelper(TableTop curTableTop,
                               PlayerInGame curPlayer,
                               SplendorActionListGenerator associatedActionGenerator){

  }
}
