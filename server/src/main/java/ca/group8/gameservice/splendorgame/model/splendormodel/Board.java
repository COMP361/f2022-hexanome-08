package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;

/**
 * Interface for board for the three boards
 */
public class Board {

  private final Card[][] cardBoard;
  private final int columns;
  private final int rows;

  public Board(int paramWidth, int paramHeight) {
    columns = paramWidth;
    rows = paramHeight;
    cardBoard = new Card[rows][columns];
    //initialise method needed

  }

  public int getColumns() {
    return columns;
  }

  public int getRows() {
    return rows;
  }

  public void add(int row, int column, Card card){
    cardBoard[row][column] = card;
  }

  public Card getCard(int row,int column) {
    return cardBoard[row][column];
  }

  boolean hasCard(Card paramCard) {
    for (Card[] array : cardBoard) {
      for (Card card : array) {
        if (card.equals(paramCard)) {
          return true;
        }
      }
    }
    return false;
  }


  public Position getCardPosition(Card paramCard) {
    if (!hasCard(paramCard)) {
      throw new IllegalArgumentException("aaaahhhh");
    }

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (cardBoard[i][j].equals(paramCard)) {
          return new Position(j, i);
        }
      }
    }
    return null;
  }


  public Card takeAndReplaceCard(Card paramCard, Position paramPosition) {
    Card takenCard = cardBoard[paramPosition.getY()][paramPosition.getX()];
    cardBoard[paramPosition.getY()][paramPosition.getX()] = paramCard;
    return takenCard;
  }

  /**
   *
   * @return a list of all cards in the board
   */
  public ArrayList<Card> getCards() {
    ArrayList<Card> allCards = new ArrayList<>();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        allCards.add(cardBoard[i][j]);
      }
    }
    return allCards;
  }
}
