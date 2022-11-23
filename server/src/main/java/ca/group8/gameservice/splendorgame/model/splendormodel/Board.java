package ca.group8.gameservice.splendorgame.model.splendormodel;

/**
 * Interface for board for the three boards
 */
public class Board {
  private int columns;
  private int rows;
  private final Card[][] cardBoard;

  public Board(int paramWidth, int paramHeight){
    columns =paramWidth;
    rows=paramHeight;
    cardBoard = new Card[rows][columns];

  }

  boolean hasCard(Card paramCard){
    for(Card[] array : cardBoard){
      for(Card card:array){
        if(card==paramCard) return true;
      }
    }
    return false;
  }

  //missing an initialise method
  Position getCardPosition(Card paramCard){
    if(!hasCard(paramCard)){
      throw new IllegalArgumentException("aaaahhhh");
    }

    for(int i=0; i<rows; i++){
      for(int j=0; j<columns; j++){
        if(cardBoard[i][j]==paramCard){
          return new Position(j,i);
        }
      }
    }
    return null;

  }

  Card takeCard(Position paramPosition){
    return null;
  };

  Card takeAndReplaceCard(Card paramCard, Position paramPosition){
    return null;
  }

}
