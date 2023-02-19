package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;

public class ClaimNobleAction extends Action {

  private Card curCard;
  private Position curPosition;

  public ClaimNobleAction(Card card, Position position) {
    assert card != null && curPosition != null;
    curCard = card;
    curPosition = position;
  }

  @Override
  void execute(TableTop curTableTop, PlayerInGame playerInGame,
               SplendorActionListGenerator actionListGenerator,
               SplendorActionInterpreter actionInterpreter) {

  }

  @Override
  Card getCurCard() {
    assert curCard != null;
    return curCard;
  }

  @Override
  Position getCardPosition() {
    assert curPosition != null;
    return curPosition;
  }
}
