package ca.group8.gameservice.splendorgame.model.splendormodel;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.ResourceUtils;

/**
 * Everything visible on the table when playing a board game.
 */
public class TableTop implements BroadcastContent {

  private final Map<Extension, Board> gameBoards;
  private final Bank bank;

  /**
   * Constructor.
   */
  public TableTop(List<String> playerNames, List<Extension> gameExtensions) {
    int playerCount = playerNames.size();
    this.bank = new Bank(playerCount);
    gameBoards = new HashMap<>();
    for (Extension extension : gameExtensions) {
      Board curBoard = createBoard(extension, playerNames);
      gameBoards.put(extension, curBoard);
    }
  }


  /**
   * Create the board according to extension name and number of players playing.
   *
   * @param curExtension extension name
   * @param playerNames number of players in game
   * @return a corresponding implementation of Board
   */
  private Board createBoard(Extension curExtension, List<String> playerNames) {
    Board resultBoard = null;
    switch (curExtension) {
      case BASE -> resultBoard = new BaseBoard(playerNames);
      case ORIENT -> resultBoard = new OrientBoard(playerNames);
      case TRADING_POST -> resultBoard = new TraderBoard(playerNames);
      case CITY -> resultBoard = new CityBoard(playerNames);
    }
    return resultBoard;
  }

  /**
   * Get the Board instance according to name of extension.
   *
   * @param curExtension extension as key to look up in the map
   * @return the corresponding board instance
   */
  public Board getBoard(Extension curExtension) {
    assert gameBoards.containsKey(curExtension);
    return gameBoards.get(curExtension);
  }


  /**
   * Used for long pulling library.
   *
   * @return always false for the sake of simplicity
   */
  @Override
  public boolean isEmpty() {
    return false;
  }

  /**
   * Getter for bank.
   *
   * @return the bank
   */
  public Bank getBank() {
    return bank;
  }

}
