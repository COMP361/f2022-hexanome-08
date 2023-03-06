package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.Power;
import ca.group8.gameservice.splendorgame.model.splendormodel.PowerEffect;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import ca.group8.gameservice.splendorgame.model.splendormodel.TraderBoard;
import java.util.Map;

/**
 * This class represents the Extra Actions associated with a specific PowerEffect.
 */
public class BonusTokenPowerAction extends Action {

  private Colour colour;
  private PlayerInGame player;

  public BonusTokenPowerAction(PlayerInGame player, Colour colour) {
    this.player = player;
    this.colour = colour;
  }

  @Override
  void execute(TableTop curTableTop, PlayerInGame playerInGame, ActionGenerator actionListGenerator,
      ActionInterpreter actionInterpreter) {
    Map<Colour,Integer> playerTokens = player.getTokenHand().getAllTokens();
    int oldTokenCount = playerTokens.get(colour);
    playerTokens.put(colour, oldTokenCount+1);
    Map<Colour,Integer> bankTokens = curTableTop.getBank().getAllTokens();
    int oldBankTokenCount = bankTokens.get(colour);
    bankTokens.put(colour,oldBankTokenCount-1);
  }

  @Override
  Card getCurCard() {
    return null;
  }

  @Override
  Position getCardPosition() throws NullPointerException {
    return null;
  }

  public Colour getColour() {
    return colour;
  }

  public PlayerInGame getPlayer() {
    return player;
  }

//  @Override
//    // TODO
//  public void execute(TableTop curTableTop, PlayerInGame playerInGame,
//               ActionGenerator actionListGenerator,
//               ActionInterpreter actionInterpreter) {
//
//    switch (powerEffect) {
//      //TODO: no default case... added for checkstyle purposes
//      default:
//      case FIVE_POINTS:
//        playerInGame.changePrestigePoints(5);
//        break;
//      case ARM_POINTS:
//        int pointsToAdd = 0;
//        TraderBoard traderBoard = (TraderBoard) curTableTop.getBoard(Extension.TRADING_POST);
//        Map<PowerEffect, Power> playerPowers =
//            traderBoard.getAllPlayerPowers().get(playerInGame.getName());
//        //Loop through all of this Player's powers. If power is unlocked, increment count by 1.
//        for (PowerEffect power : PowerEffect.values()) {
//          if (playerPowers.get(power).isUnlocked()) {
//            pointsToAdd += 1;
//          }
//        }
//        playerInGame.changePrestigePoints(pointsToAdd);
//        //TODO: Add cases for creating cascading actions for other 3 powers.
//    }
//
//  }
//
//  @Override
//  Card getCurCard() throws NullPointerException {
//    throw new NullPointerException("There is no card associated with this action.");
//  }
//
//  @Override
//    //TODO: Should this have a curCard position associated with it??
//  Position getCardPosition() throws NullPointerException {
//    throw new NullPointerException("There is no card position associated with this action.");
//  }
}
