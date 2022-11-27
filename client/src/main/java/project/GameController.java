package project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import project.view.splendor.*;
import project.view.splendor.gameitems.BaseCard;
import project.view.splendor.gameitems.DevelopmentCard;

/**
 * Game controller for game GUI.
 */
public class GameController {


  @FXML
  private AnchorPane playerBoardAnchorPane;

  @FXML
  private VBox baseCardDeck;

  @FXML
  private VBox orientCardDeck;
  /**
   * Opening the development cards pop up once "My Cards" button is pressed.
   */

  private final Map<Integer, Map<CardType, DevelopmentCardBoardGui>>
      levelCardsMap = new HashMap<>();

  private final Map<String, PlayerInfoGui> nameToPlayerInfoGuiMap = new HashMap<>();

  private List<String> sortedPlayerNames;

  @FXML
  protected void onExitGameClick() throws IOException {
    App.setRoot("admin_lobby_page");
  }

  @FXML
  protected void onOpenMyReserveCardClick() throws IOException {
    App.setRootWithSizeTitle("my_reserved_cards", 789, 406, "My Reserved Cards");
  }

  @FXML
  protected void onOpenMyPurchaseCardClick() throws IOException {
    App.setRootWithSizeTitle("my_development_cards", 800, 600, "My Development Cards");
  }


  private List<String> sortPlayerNames(String curPlayerName, List<String> allPlayerNames) {
    while(!allPlayerNames.get(0).equals(curPlayerName)) {
      String tmpPlayerName = allPlayerNames.remove(0);
      allPlayerNames.add(tmpPlayerName);
    }
    return new ArrayList<>(allPlayerNames);
  }
  private Map<PlayerPosition, String> setPlayerToPosition(String curPlayerName, List<String> allPlayerNames) {
    Map<PlayerPosition, String> resultMap = new HashMap<>();
    List<String> orderedNames = sortPlayerNames(curPlayerName, allPlayerNames);
    for (int i = 0; i < orderedNames.size(); i++) {
      resultMap.put(PlayerPosition.values()[i], orderedNames.get(i));
    }
    return resultMap;
  }

  public void initialize() {

    GameBoardLayoutConfig config = App.getGuiLayouts();
    // TODO: change based on number of players, get the info from server later
    String firstPlayer = "D"; // needs to highlight the boarder of this player
    String curPlayer = "A";
    String[] allPlayerNames = new String[] {"C","A", "D", "B"};
    List<String> playerNames = new ArrayList<>(Arrays.asList(allPlayerNames));
    sortedPlayerNames = sortPlayerNames(curPlayer, playerNames);
    int curPlayerNum = playerNames.size();

    // initialize noble area
    NobleBoardGui nobleBoard = new NobleBoardGui(100,100,5);
    Platform.runLater(() -> {
      nobleBoard.setup(curPlayerNum,config.getNobleLayoutX(), config.getNobleLayoutY());
      playerBoardAnchorPane.getChildren().add(nobleBoard);
    });

    // initialize token area
    TokenBankGui tokenBank = new TokenBankGui();
    Platform.runLater(() -> {
      tokenBank.setup(curPlayerNum,config.getTokenBankLayoutX(),config.getTokenBankLayoutY());
      playerBoardAnchorPane.getChildren().add(tokenBank);
    });

    // initialize player area
    List<VerticalPlayerInfoGui> verticalPlayers = new ArrayList<>();
    List<HorizontalPlayerInfoGui> horizontalPlayers = new ArrayList<>();
    if (curPlayerNum >= 2) {
      String btmPlayerName = sortedPlayerNames.get(0);
      String leftPlayerName = sortedPlayerNames.get(1);
      HorizontalPlayerInfoGui btmPlayerGui =
          new HorizontalPlayerInfoGui(PlayerPosition.BOTTOM, btmPlayerName, 3);
      VerticalPlayerInfoGui leftPlayerGui =
          new VerticalPlayerInfoGui(PlayerPosition.LEFT, leftPlayerName, 3);
      btmPlayerGui.setup(config.getBtmPlayerLayoutX(),config.getBtmPlayerLayoutY());
      leftPlayerGui.setup(config.getLeftPlayerLayoutX(),config.getLeftPlayerLayoutY());
      horizontalPlayers.add(btmPlayerGui);
      verticalPlayers.add(leftPlayerGui);
      nameToPlayerInfoGuiMap.put(btmPlayerName, btmPlayerGui);
      nameToPlayerInfoGuiMap.put(leftPlayerName, leftPlayerGui);
      if (curPlayerNum >= 3) {
        String topPlayerName = sortedPlayerNames.get(2);
        HorizontalPlayerInfoGui topPlayerGui =
            new HorizontalPlayerInfoGui(PlayerPosition.TOP, topPlayerName, 3);
        topPlayerGui.setup(config.getTopPlayerLayoutX(),config.getTopPlayerLayoutY());
        horizontalPlayers.add(topPlayerGui);
        nameToPlayerInfoGuiMap.put(topPlayerName, topPlayerGui);
        if (curPlayerNum == 4) {
          String rightPlayerName = sortedPlayerNames.get(3);
          VerticalPlayerInfoGui rightPlayerGui =
              new VerticalPlayerInfoGui(PlayerPosition.RIGHT, rightPlayerName, 3);
          rightPlayerGui.setup(config.getRightPlayerLayoutX(),config.getRightPlayerLayoutY());
          verticalPlayers.add(rightPlayerGui);
          nameToPlayerInfoGuiMap.put(rightPlayerName, rightPlayerGui);
        }
      }
    }

    Platform.runLater(() -> {
      for (VerticalPlayerInfoGui vPlayer : verticalPlayers) {
        playerBoardAnchorPane.getChildren().add(vPlayer);
      }

      for (HorizontalPlayerInfoGui hPlayer : horizontalPlayers) {
        playerBoardAnchorPane.getChildren().add(hPlayer);
      }
    });


    // check how to highlight which player
    // TODO: Before switching off to next player, remember to setHighlight(false) to last player
    String currentTurnPlayerName = "D";
    for (String name : nameToPlayerInfoGuiMap.keySet()) {
      if(name.equals(currentTurnPlayerName)) {
        nameToPlayerInfoGuiMap.get(name).setHighlight(true);
      }
    }

    // initialize cardboard area

    // ----------------------------------------------------------------------------------------
    // DUMMY CARDS TESTING
    EnumMap<Colour, Integer> cardPrice = new EnumMap<>(Colour.class);
    cardPrice.put(Colour.RED, 0);
    cardPrice.put(Colour.BLACK, 0);
    cardPrice.put(Colour.BLUE, 4);
    cardPrice.put(Colour.WHITE, 0);
    cardPrice.put(Colour.GREEN, 0);

    EnumMap<Colour, Integer> cardPrice2 = new EnumMap<>(Colour.class);
    cardPrice2.put(Colour.RED, 0);
    cardPrice2.put(Colour.BLACK, 0);
    cardPrice2.put(Colour.BLUE, 0);
    cardPrice2.put(Colour.WHITE, 0);
    cardPrice2.put(Colour.GREEN, 3);
    DevelopmentCard c1 = new DevelopmentCard(1,
        cardPrice, "b1", 1,
        Optional.of(Colour.BLACK),false, -1, 1);


    DevelopmentCard c2 = new DevelopmentCard(1,
        cardPrice2, "o1g1", 1,
        Optional.empty(),false, -1, 0);

    List<DevelopmentCard> cards = new ArrayList<>();
    List<DevelopmentCard> cards2 = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      cards2.add(c2);
    }
    for (int i = 0; i < 4; i++) {
      cards.add(c1);
    }
    // ----------------------------------------------------------------------------------------

    // base card and orient card area
    for (int i = 3; i >= 1; i--) {
      Map<CardType, DevelopmentCardBoardGui> oneLevelCardsMap = new HashMap<>();
      BaseCardLevelGui baseCardLevelGui = new BaseCardLevelGui(i);
      OrientCardLevelGui orientCardLevelGui = new OrientCardLevelGui(i);

      baseCardLevelGui.setup(cards);
      orientCardLevelGui.setup(cards2);
      oneLevelCardsMap.put(CardType.BASE, baseCardLevelGui);
      oneLevelCardsMap.put(CardType.ORIENT, orientCardLevelGui);

      Platform.runLater(() -> {
        baseCardDeck.getChildren().add(baseCardLevelGui);
        orientCardDeck.getChildren().add(orientCardLevelGui);
      });
      levelCardsMap.put(i, oneLevelCardsMap);
    }


  }

}
