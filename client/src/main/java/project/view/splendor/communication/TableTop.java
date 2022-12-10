package project.view.splendor.communication;

import java.util.List;


public class TableTop {

  public TableTop(List<NobleCard> nobles, BaseBoard baseBoard, Bank bank) {
    this.nobles = nobles;
    this.baseBoard = baseBoard;
    this.bank = bank;
  }

  public List<NobleCard> getNobles() {
    return nobles;
  }

  public void setNobles(List<NobleCard> nobles) {
    this.nobles = nobles;
  }

  private List<NobleCard> nobles;
  private BaseBoard baseBoard;
  private Bank bank;


  public BaseBoard getBaseBoard() {
    return baseBoard;
  }


  public Bank getBank() {
    return bank;
  }


  public void setBaseBoard(BaseBoard baseBoard) {
    this.baseBoard = baseBoard;
  }


  public void setBank(Bank bank) {
    this.bank = bank;
  }


}