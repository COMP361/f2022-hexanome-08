package project.view.splendor.communication;

import java.util.ArrayList;

/**
 * Interface for board for the three boards
 */
public class Board {

  private Card[][] cardBoard;
  private int columns;
  private int rows;

  public void setCardBoard(Card[][] cardBoard) {
    this.cardBoard = cardBoard;
  }

  public void setColumns(int columns) {
    this.columns = columns;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public Card[][] getCardBoard() {
    return cardBoard;
  }

  public int getColumns() {
    return columns;
  }

  public int getRows() {
    return rows;
  }

  public Board(int columns, int rows) {
    this.cardBoard = new Card[columns][rows];
    this.columns = columns;
    this.rows = rows;
  }
}
