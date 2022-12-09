package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Board holding nobles available this game.
 */
public class NobleBoard {

  private final Card[][] cardBoard;
  private final int columns;
  private final int rows;

  /**
   * TODO: initialize method.
   */
  public NobleBoard(int paramHeight, int paramWidth) {
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

  public void add(int row, int column, Card card) {
    cardBoard[row][column] = card;
  }

  public Card getCard(int row, int column) {
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

  /**
   * Get x,y position of a card on the board.
   */
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

  /**
   * Remove card and replace with new card from deck.
   */
  public Card takeAndReplaceCard(Card paramCard, Position paramPosition) {
    Card takenCard = cardBoard[paramPosition.getY()][paramPosition.getX()];
    cardBoard[paramPosition.getY()][paramPosition.getX()] = paramCard;
    return takenCard;
  }

  public void remove(Position paramPosition) {
    cardBoard[paramPosition.getY()][paramPosition.getX()] = null;
  }

  /**
   * return a list of all cards in the board.
   */
  public ArrayList<Card> getCards() {
    ArrayList<Card> allCards = new ArrayList<>();
    for (int i = 0; i < rows; i++) {
      allCards.addAll(Arrays.asList(cardBoard[i]).subList(0, columns));
    }
    return allCards;
  }


}
