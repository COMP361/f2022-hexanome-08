package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.PurchasedHand;
import ca.group8.gameservice.splendorgame.model.splendormodel.TokenHand;
import java.util.EnumMap;

public class PurchaseAction extends CardAction {
  int goldTokenRequired;

  public PurchaseAction(boolean isCardAction,
                        Position position,
                        Card card, int goldTokenRequired) {
    super(isCardAction, position, card);
    this.goldTokenRequired = goldTokenRequired;
  }

   public int getGoldTokenRequired(){
      return goldTokenRequired;
    }


  @Override
  public void execute(GameInfo currentGameState, PlayerInGame playerState) {
    // TODO: For now, just do a simple downcast assuming the card is DevelopmentCard
    DevelopmentCard card = (DevelopmentCard) super.getCard();
    PurchasedHand hand = playerState.getPurchasedHand();
    TokenHand tokenHand = playerState.getTokenHand();
    EnumMap<Colour, Integer> playerGems = playerState.getTotalGems();
    // update player's prestige points
    int cardPoints = card.getPrestigePoints();
    int playerCurPoints = playerState.getPrestigePoints();
    playerState.setPrestigePoints(cardPoints + playerCurPoints);

    // update player's purchase hand
    for(Colour colour:Colour.values()){
      if(colour.equals(Colour.GOLD)){
        if(goldTokenRequired > 0){
          int currentGoldTokenHeld = tokenHand.getAllTokens().get(colour);
          tokenHand.getAllTokens().put(colour, currentGoldTokenHeld - goldTokenRequired);
        }
        continue;
      }
      int discountedPrice = card.getPrice().get(colour)-playerGems.get(colour);
      if(discountedPrice>0){
        int remainingTokens = tokenHand.getAllTokens().get(colour) - discountedPrice;
        tokenHand.getAllTokens().put(colour, Math.max(remainingTokens, 0));
      }
    }
    hand.addDevelopmentCard(card);
    int level = card.getLevel();

    // replace the card with another new card draw from deck
    Card newCard = currentGameState.getTableTop().getDecks().get(level).pop();
    Position curCardPosition = super.getPosition();
    currentGameState.getTableTop().getBaseBoard().takeAndReplaceCard(newCard, curCardPosition);
  }
}
