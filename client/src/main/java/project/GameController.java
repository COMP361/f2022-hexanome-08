package project;


import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.model.Colour;
import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.commons.codec.digest.DigestUtils;
import project.connection.GameRequestSender;
import project.view.lobby.communication.Session;
import project.view.lobby.communication.User;
import project.view.splendor.BaseCardLevelGui;
import project.view.splendor.HorizontalPlayerInfoGui;
import project.view.splendor.NobleBoardGui;
import project.view.splendor.PlayerInfoGui;
import project.view.splendor.PlayerPosition;
import project.view.splendor.TokenBankGui;
import project.view.splendor.VerticalPlayerInfoGui;
import ca.mcgill.comp361.splendormodel.model.*;

/**
 * Game controller for game GUI.
 */
public class GameController implements Initializable {


  @FXML
  private AnchorPane playerBoardAnchorPane;

  @FXML
  private VBox baseCardBoard;

  @FXML
  private VBox orientCardBoard;

  @FXML
  private Button myCardButton;

  @FXML
  private Button myReservedCardsButton;

  // TODO: assign function to it later
  @FXML
  private Button quitButton;

  private final long gameId;

  private final Session curSession;

  private NobleBoardGui nobleBoard;

  private TokenBankGui tokenBankGui;

  private String prePlayerName;

  private final Map<Integer, BaseCardLevelGui> baseCardGuiMap = new HashMap<>();

  private final Map<String, PlayerInfoGui> nameToPlayerInfoGuiMap = new HashMap<>();

  private final Map<String, Integer> nameToArmCodeMap = new HashMap<>();

  private List<String> sortedPlayerNames = new ArrayList<>();

  public GameController(long gameId, Session curSession) {
    this.gameId = gameId;
    this.curSession = curSession;
  }


  /**
   * Opening the development cards pop up once "My Cards" button is pressed.
   */

  @FXML
  protected void onExitGameClick() throws IOException {
    App.setRoot("admin_lobby_page");
  }


  private EventHandler<ActionEvent> createOpenMyReserveCardClick() {
    return event -> {
      GameRequestSender sender = App.getGameRequestSender();
      String curPlayerName = App.getUser().getUsername();
      String playerStatsJson = sender.sendGetAllPlayerInfoRequest(gameId, "").getBody();
      Gson gsonParser = SplendorDevHelper.getInstance().getGson();
      PlayerStates playerStates = gsonParser.fromJson(playerStatsJson, PlayerStates.class);
      // every time button click, we have up-to-date information
      PlayerInGame playerInGame = playerStates.getOnePlayerInGame(curPlayerName);
      ReservedHand reservedHand = playerInGame.getReservedHand();
      String gameInfoJson = sender.sendGetGameInfoRequest(gameId, "").getBody();
      GameInfo gameInfo = gsonParser.fromJson(gameInfoJson, GameInfo.class);
      String playerName = App.getUser().getUsername();
      Map<String, Action> playerActions = gameInfo.getPlayerActionMaps().get(playerName);

      try {
        App.loadPopUpWithController("my_reserved_cards.fxml",
            new ReservedHandController(reservedHand, playerActions),
            800,
            600);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    };
  }

  /**
   * Whenever MyPurchaseCard button is clicked, we load "my_development_cards.fxml".
   * The gui display logic is controlled by a PurchaseHandController.
   * We shall get a new PlayerInGame for curPlayer everytime the player clicks on this button.
   *
   * @return the event defined to handle the assign controller and send requests.
   */
  private EventHandler<ActionEvent> createOpenMyPurchaseCardClick() {
    return event -> {
      GameRequestSender sender = App.getGameRequestSender();
      String curPlayerName = App.getUser().getUsername();
      String playerStatsJson = sender.sendGetAllPlayerInfoRequest(gameId, "").getBody();
      Gson gsonParser = SplendorDevHelper.getInstance().getGson();
      PlayerStates playerStates = gsonParser.fromJson(playerStatsJson, PlayerStates.class);
      // every time button click, we have up-to-date information
      PlayerInGame playerInGame = playerStates.getOnePlayerInGame(curPlayerName);
      PurchasedHand purchasedHand = playerInGame.getPurchasedHand();
      String gameInfoJson = sender.sendGetGameInfoRequest(gameId, "").getBody();
      GameInfo gameInfo = gsonParser.fromJson(gameInfoJson, GameInfo.class);
      String playerName = App.getUser().getUsername();
      Map<String, Action> playerActions = gameInfo.getPlayerActionMaps().get(playerName);

      try {
        App.loadPopUpWithController(
            "my_development_cards.fxml",
            new PurchaseHandController(purchasedHand, playerActions),
            800,
            600);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    };
  }

  private List<String> sortPlayerNames(String curPlayerName, List<String> allPlayerNames) {
    while (!allPlayerNames.get(0).equals(curPlayerName)) {
      String tmpPlayerName = allPlayerNames.remove(0);
      allPlayerNames.add(tmpPlayerName);
    }
    return new ArrayList<>(allPlayerNames);
  }

  private Map<PlayerPosition, String> setPlayerToPosition(String curPlayerName,
                                                          List<String> allPlayerNames) {
    Map<PlayerPosition, String> resultMap = new HashMap<>();
    List<String> orderedNames = sortPlayerNames(curPlayerName, allPlayerNames);
    for (int i = 0; i < orderedNames.size(); i++) {
      resultMap.put(PlayerPosition.values()[i], orderedNames.get(i));
    }
    return resultMap;
  }


  private void updatePlayerInfoGui(PlayerInGame curPlayerInventory) {

    String playerName = curPlayerInventory.getName();
    int newPoints = curPlayerInventory.getPrestigePoints();
    EnumMap<Colour, Integer> newTokenInHand = curPlayerInventory.getTokenHand().getAllTokens();
    List<DevelopmentCard> allDevCards =
        curPlayerInventory.getPurchasedHand().getDevelopmentCards();
    // Get the player gui
    PlayerInfoGui playerInfoGui = nameToPlayerInfoGuiMap.get(playerName);
    // updating the GUI based on the new info from server
    Platform.runLater(() -> {
      // update the public-player associated area
      // TODO: Add updating number of noble reserved and number of dev cards reserved
      playerInfoGui.setNewPrestigePoints(newPoints);
      playerInfoGui.setNewTokenInHand(newTokenInHand);
      playerInfoGui.setGemsInHand(allDevCards);
    });
  }


  private Thread generateAllPlayerInfoUpdateThread(
      GameRequestSender gameRequestSender, GameInfo firstGameInfo) {
    return new Thread(() -> {
      String hashedResponse = "";
      HttpResponse<String> longPullResponse = null;
      boolean isFirstCheck = true;
      while (true) {
        int responseCode = 408;
        while (responseCode == 408) {
          longPullResponse = gameRequestSender.sendGetAllPlayerInfoRequest(gameId, hashedResponse);
          responseCode = longPullResponse.getStatus();
        }

        if (responseCode == 200) {
          hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
          // decode this response into PlayerInGame class with Gson
          String responseInJsonString = longPullResponse.getBody();
          Gson splendorParser = SplendorDevHelper.getInstance().getGson();
          PlayerStates playerStates = splendorParser
              .fromJson(responseInJsonString, PlayerStates.class);
          if (isFirstCheck) {
            try {
              setupAllPlayerInfoGui(playerStates,
                  0,
                  firstGameInfo.getFirstPlayerName());
            } catch (UnirestException e) {
              throw new RuntimeException(e);
            }

            isFirstCheck = false;
          } else {
            for (PlayerInGame playerInfo : playerStates.getPlayersInfo().values()) {
              if (playerInfo.getName().equals(App.getUser().getUsername())) {
                myCardButton.setOnAction(createOpenMyPurchaseCardClick());
                myReservedCardsButton.setOnAction(createOpenMyReserveCardClick());
              }
              updatePlayerInfoGui(playerInfo);
            }
          }
        }
      }
    });
  }


  private void setupAllPlayerInfoGui(PlayerStates playerStates, int initTokenAmount,
                                     String firstPlayer)
      throws UnirestException {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    User curUser = App.getUser();
    // first check, then sort the player names accordingly based on different clients
    List<String> playerNames = new ArrayList<>(playerStates.getPlayersInfo().keySet());
    if (sortedPlayerNames.isEmpty()) {
      sortedPlayerNames = sortPlayerNames(App.getUser().getUsername(), playerNames);
    }
    // btmPlayer Gui is equivalent to current player gui
    // if this map is empty, then we are not playing
    HorizontalPlayerInfoGui btmPlayerGui;
    String curPlayerName = curUser.getUsername();
    if (nameToArmCodeMap.isEmpty()) {
     btmPlayerGui = new HorizontalPlayerInfoGui(PlayerPosition.BOTTOM,
         curPlayerName,
         initTokenAmount,
         -1);
     // armCode with -1 which means we will not get the arm image view to display anything
    } else {
      // we are playing Trader extension, need to set the arm code accordingly
      int armCode = nameToArmCodeMap.get(curPlayerName);
      btmPlayerGui = new HorizontalPlayerInfoGui(PlayerPosition.BOTTOM,
          curPlayerName,
          initTokenAmount,
          armCode);
    }
    // store the GUI to a map so that it's easier to access later
    nameToPlayerInfoGuiMap.put(curUser.getUsername(), btmPlayerGui);
    btmPlayerGui.setup(config.getBtmPlayerLayoutX(), config.getBtmPlayerLayoutY());
    List<HorizontalPlayerInfoGui> horizontalPlayers = new ArrayList<>();
    horizontalPlayers.add(btmPlayerGui);

    // The My PurchaseHand and My Reserve Hand buttons functionality assign
    // Do not do long pulling here (hash = "" -> instant response)
    myCardButton.setOnAction(createOpenMyPurchaseCardClick());
    myReservedCardsButton.setOnAction(createOpenMyReserveCardClick());

    // set up other player area with data from server
    String leftPlayerName = sortedPlayerNames.get(1);
    List<VerticalPlayerInfoGui> verticalPlayers = new ArrayList<>();

    // same logic as setting up horizontal player GUI, check if we need to display arm type
    VerticalPlayerInfoGui leftPlayerGui;
    if (nameToArmCodeMap.isEmpty()) {
      leftPlayerGui = new VerticalPlayerInfoGui(PlayerPosition.LEFT,
          leftPlayerName,
          initTokenAmount,
          -1);
      // armCode with -1 which means we will not get the arm image view to display anything
    } else {
      // we are playing Trader extension, need to set the arm code accordingly
      int armCode = nameToArmCodeMap.get(leftPlayerName);
      leftPlayerGui = new VerticalPlayerInfoGui(PlayerPosition.LEFT,
          leftPlayerName,
          initTokenAmount,
          armCode);
    }
    // set up
    leftPlayerGui.setup(config.getLeftPlayerLayoutX(), config.getLeftPlayerLayoutY());
    // add to array, so that we can add them to gui at the same time later
    verticalPlayers.add(leftPlayerGui);
    // put them into global map for !firstCheck case
    nameToPlayerInfoGuiMap.put(leftPlayerName, leftPlayerGui);

    // now if we have 3 players or more, then more gui need to be added
    int curPlayersCount = sortedPlayerNames.size();
    if (curPlayersCount >= 3) {
      String topPlayerName = sortedPlayerNames.get(2);
      HorizontalPlayerInfoGui topPlayerGui;

      if (nameToArmCodeMap.isEmpty()) {
        topPlayerGui = new HorizontalPlayerInfoGui(PlayerPosition.TOP,
            topPlayerName,
            initTokenAmount,
            -1);
        // armCode with -1 which means we will not get the arm image view to display anything
      } else {
        // we are playing Trader extension, need to set the arm code accordingly
        int armCode = nameToArmCodeMap.get(topPlayerName);
        topPlayerGui = new HorizontalPlayerInfoGui(PlayerPosition.TOP,
            topPlayerName,
            initTokenAmount,
            armCode);
      }
      topPlayerGui.setup(config.getTopPlayerLayoutX(), config.getTopPlayerLayoutY());
      horizontalPlayers.add(topPlayerGui);
      nameToPlayerInfoGuiMap.put(topPlayerName, topPlayerGui);


      if (curPlayersCount == 4) {
        String rightPlayerName = sortedPlayerNames.get(3);
        VerticalPlayerInfoGui rightPlayerGui;
        if (nameToArmCodeMap.isEmpty()) {
          rightPlayerGui = new VerticalPlayerInfoGui(PlayerPosition.RIGHT,
              rightPlayerName,
              initTokenAmount,
              -1);
          // armCode with -1 which means we will not get the arm image view to display anything
        } else {
          // we are playing Trader extension, need to set the arm code accordingly
          int armCode = nameToArmCodeMap.get(rightPlayerName);
          rightPlayerGui = new VerticalPlayerInfoGui(PlayerPosition.RIGHT,
              rightPlayerName,
              initTokenAmount,
              armCode);
        }
        rightPlayerGui.setup(config.getRightPlayerLayoutX(), config.getRightPlayerLayoutY());
        verticalPlayers.add(rightPlayerGui);
        nameToPlayerInfoGuiMap.put(rightPlayerName, rightPlayerGui);
      }
    }

    // now the gui is set, add them
    Platform.runLater(() -> {
      for (VerticalPlayerInfoGui verticalPlayer : verticalPlayers) {
        playerBoardAnchorPane.getChildren().add(verticalPlayer);
      }

      for (HorizontalPlayerInfoGui horizontalPlayer : horizontalPlayers) {
        playerBoardAnchorPane.getChildren().add(horizontalPlayer);
      }
    });
    // since it's in the first check, curTurn and firstPlayer are same, highlight
    // first player
    prePlayerName = firstPlayer;
    // at this point, the map can not be empty, safely get the playerGui
    nameToPlayerInfoGuiMap.get(firstPlayer).setHighlight(true);
  }



  //private void assignActionsToCardBoard() throws UnirestException {
  //  SplendorServiceRequestSender gameRequestSender = App.getGameRequestSender();
  //  User curUser = App.getUser();
  //  HttpResponse<String> actionMapResponse =
  //      gameRequestSender.sendGetPlayerActionsRequest(gameId, curUser.getUsername(),
  //          curUser.getAccessToken());
  //  Type actionMapType = new TypeToken<Map<String, Action>>() {
  //  }.getType();
  //  Gson actionGson = GameController.getActionGson();
  //  Map<String, Action> resultActionsMap =
  //      actionGson.fromJson(actionMapResponse.getBody(), actionMapType);
  //
  //  // if the action map is not empty, assign functions to the cards
  //  String[][][] actionHashesLookUp = new String[3][4][2];
  //  if (!resultActionsMap.isEmpty()) {
  //    // if result action map is not empty, we need to assign hash values to it
  //    for (String actionHash : resultActionsMap.keySet()) {
  //      Action curAction = resultActionsMap.get(actionHash);
  //      if (curAction.checkIsCardAction()) {
  //        if (!(curAction.getCard() == null) && !(curAction.getPosition() == null)) {
  //          Position curPosition = curAction.getPosition();
  //          int level = curPosition.getCoordinateX();
  //          int cardIndex = curPosition.getCoordinateY();
  //          if (curAction instanceof PurchaseAction) {
  //            actionHashesLookUp[3 - level][cardIndex][0] = actionHash;
  //          } else {
  //            actionHashesLookUp[3 - level][cardIndex][1] = actionHash;
  //          }
  //        }
  //
  //      } else {
  //        // TODO: LATER, TAKE TOKEN ACTION OR SOMETHING ELSE
  //      }
  //    }
  //  }
  //  System.out.println("All actions: " + resultActionsMap);
  //
  //  // Since we now have all String[2] actionHashes, we can go and
  //  // assign them to cards
  //  for (int i = 0; i < 3; i++) {
  //    if (resultActionsMap.isEmpty()) {
  //      // if it's empty, reset the values
  //      actionHashesLookUp[i] = new String[4][2];
  //    }
  //    baseCardGuiMap
  //        .get(3 - i)
  //        .bindActionToCardAndDeck(actionHashesLookUp[i], gameId);
  //  }
  //}

  //
  //private Thread generateGameInfoUpdateThread(SplendorServiceRequestSender gameRequestSender) {
  //  GameBoardLayoutConfig config = App.getGuiLayouts();
  //  User curUser = App.getUser(); // at this point, user will not be Null
  //
  //  return new Thread(() -> {
  //    // basic stuff needed for a long pull
  //    String hashedResponse = "";
  //    HttpResponse<String> longPullResponse = null;
  //    boolean isFirstCheck = true;
  //
  //    while (true) {
  //      // always do one, just in case
  //      try {
  //        App.refreshUserToken(curUser);
  //        // after this, curUser.getAccessToken() will for sure have a valid token
  //      } catch (UnirestException e) {
  //        throw new RuntimeException(e);
  //      }
  //
  //      int responseCode = 408;
  //      while (responseCode == 408) {
  //        try {
  //          longPullResponse = gameRequestSender.sendGetGameInfoRequest(gameId, hashedResponse);
  //        } catch (UnirestException e) {
  //          throw new RuntimeException(e);
  //        }
  //        responseCode = longPullResponse.getStatus();
  //      }
  //
  //      if (responseCode == 200) {
  //        // update the MD5 hash of previous response
  //        hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
  //        // decode this response into GameInfo class with Gson
  //        String responseInJsonString = longPullResponse.getBody();
  //        GameInfo curGameInfo = new Gson().fromJson(responseInJsonString, GameInfo.class);
  //        // how we are going to parse from this curGameInfo depends on first check or not
  //        if (isFirstCheck) {
  //          // TODO: Potential nested clickable noble cards
  //          // initialize noble area
  //          nobleBoard = new NobleBoardGui(100, 100, 5);
  //          List<NobleCard> nobles = curGameInfo.getTableTop().getNobles();
  //          Platform.runLater(() -> {
  //            nobleBoard.setup(nobles, config.getNobleLayoutX(), config.getNobleLayoutY(), true);
  //            playerBoardAnchorPane.getChildren().add(nobleBoard);
  //          });
  //
  //          // initialize token area
  //          tokenBankGui = new TokenBankGui();
  //          EnumMap<Colour, Integer> bankBalance =
  //              curGameInfo.getTableTop().getBank().getAllTokens();
  //          Platform.runLater(() -> {
  //            tokenBankGui.setup(bankBalance,
  //                config.getTokenBankLayoutX(),
  //                config.getTokenBankLayoutY(), true);
  //            playerBoardAnchorPane.getChildren().add(tokenBankGui);
  //          });
  //
  //          // initialize the base board
  //          for (int i = 3; i >= 1; i--) {
  //            List<BaseCard> oneLevelCards =
  //                curGameInfo.getTableTop().getBaseBoard().getBaseCardsOnBoard().get(i);
  //            List<BaseCard> oneLevelDeck =
  //                curGameInfo.getTableTop().getBaseBoard().getBaseDecks().get(i);
  //            BaseCardLevelGui baseCardLevelGui =
  //                new BaseCardLevelGui(i, oneLevelCards, oneLevelDeck);
  //            baseCardLevelGui.setup();
  //            Platform.runLater(() -> {
  //              baseCardBoard.getChildren().add(baseCardLevelGui);
  //            });
  //            baseCardGuiMap.put(i, baseCardLevelGui);
  //          }
  //          try {
  //            System.out.println(
  //                "First time generate actions for player: " + curUser.getUsername());
  //            assignActionsToCardBoard();
  //          } catch (UnirestException e) {
  //            throw new RuntimeException(e);
  //          }
  //          isFirstCheck = false;
  //        } else { // If it is not first check.....
  //          // need to display
  //          // TODO:
  //          //  1. NEW Cards on board (and their actions)
  //          //  2. New Nobles on board (and their actions) DONE
  //          //  3. New token bank info (and action?) DONE
  //
  //          // First step, change the highlight (if prePlayer and curren player does not match)
  //          String currentPlayerName = curGameInfo.getCurrentPlayer();
  //          if (!prePlayerName.equals(currentPlayerName)) {
  //            nameToPlayerInfoGuiMap.get(prePlayerName).setHighlight(false);
  //            nameToPlayerInfoGuiMap.get(currentPlayerName).setHighlight(true);
  //            prePlayerName = currentPlayerName;
  //          } // if they are the same, no need to change the height colour
  //
  //          List<NobleCard> nobles = curGameInfo.getTableTop().getNobles();
  //          Platform.runLater(() -> {
  //            nobleBoard.setup(nobles,
  //                config.getNobleLayoutX(),
  //                config.getNobleLayoutY(), false);
  //          });
  //
  //          EnumMap<Colour, Integer> bankBalance =
  //              curGameInfo.getTableTop().getBank().getAllTokens();
  //          Platform.runLater(() -> {
  //            tokenBankGui.setup(bankBalance,
  //                config.getTokenBankLayoutX(),
  //                config.getTokenBankLayoutY(), false);
  //          });
  //
  //          // update the cards GUI
  //          for (int i = 3; i >= 1; i--) {
  //            BaseCardLevelGui oneLevelCardsGui = baseCardGuiMap.get(i);
  //            List<BaseCard> oneLevelCards =
  //                curGameInfo.getTableTop().getBaseBoard().getBaseCardsOnBoard().get(i);
  //            List<BaseCard> oneLevelDeck =
  //                curGameInfo.getTableTop().getBaseBoard().getBaseDecks().get(i);
  //            oneLevelCardsGui.setCards(oneLevelCards);
  //            oneLevelCardsGui.setDeck(oneLevelDeck);
  //            oneLevelCardsGui.setup();
  //          }
  //          try {
  //            System.out.println("Updating actions for player: " + curUser.getUsername());
  //            assignActionsToCardBoard();
  //          } catch (UnirestException e) {
  //            throw new RuntimeException(e);
  //          }
  //        }
  //      }
  //    }
  //  });
  //}

  @Override
  // TODO: This method contains what's gonna happen after clicking "play" on the board
  public void initialize(URL url, ResourceBundle resourceBundle) {
    GameRequestSender gameRequestSender = App.getGameRequestSender();
    System.out.println("Current user: " + App.getUser().getUsername());
    System.out.println(gameRequestSender.getGameServiceName());

    HttpResponse<String> firstGameInfoResponse =
        gameRequestSender.sendGetGameInfoRequest(gameId, "");
    Gson gsonParser = SplendorDevHelper.getInstance().getGson();
    GameInfo curGameInfo = gsonParser.fromJson(firstGameInfoResponse.getBody(), GameInfo.class);
    List<Extension> extensionsPlaying = curGameInfo.getExtensions();
    List<String> playerNames = curGameInfo.getPlayerNames();
    // if we are playing the Trading Extension, initialize the map of player name
    if (extensionsPlaying.contains(Extension.TRADING_POST)) {
      for (int i = 1; i <= playerNames.size(); i++) {
        nameToArmCodeMap.put(playerNames.get(i-1), i);
      }
    }
    TableTop firstTableTop = curGameInfo.getTableTop();
    Thread playerInfoRelatedThread =
        generateAllPlayerInfoUpdateThread(gameRequestSender, curGameInfo);
    //Thread mainGameUpdateThread = generateGameInfoUpdateThread(gameRequestSender);
    //// start the thread for main board and playerInfo update at the same time
    playerInfoRelatedThread.start();
    //mainGameUpdateThread.start();

  }
}
