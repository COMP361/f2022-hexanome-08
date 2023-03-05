package project;


import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.model.Colour;
import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.lang.reflect.Type;
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
import project.view.splendor.BaseBoardGui;
import project.view.splendor.BaseCardLevelGui;
import project.view.splendor.BoardGui;
import project.view.splendor.CityBoardGui;
import project.view.splendor.HorizontalPlayerInfoGui;
import project.view.splendor.NobleBoardGui;
import project.view.splendor.OrientBoardGui;
import project.view.splendor.PlayerInfoGui;
import project.view.splendor.PlayerPosition;
import project.view.splendor.TokenBankGui;
import project.view.splendor.TraderBoardGui;
import project.view.splendor.VerticalPlayerInfoGui;
import ca.mcgill.comp361.splendormodel.model.*;

/**
 * Game controller for game GUI.
 */
public class GameController implements Initializable {


  @FXML
  private AnchorPane playerBoardAnchorPane;

  @FXML
  // the VBox that we want to update on to store the base cards (cards only)
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

  private final Map<Extension, BoardGui> extensionBoardGuiMap = new HashMap<>();

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


  /**
   * Update one PlayerInfoGui based on one PlayerInGame object.
   *
   * @param curPlayerInGame
   */
  private void updatePlayerInfoGui(PlayerInGame curPlayerInGame) {

    String playerName = curPlayerInGame.getName();
    int newPoints = curPlayerInGame.getPrestigePoints();
    EnumMap<Colour, Integer> newTokenInHand = curPlayerInGame.getTokenHand().getAllTokens();
    List<DevelopmentCard> allDevCards =
        curPlayerInGame.getPurchasedHand().getDevelopmentCards();
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


  private Thread generateAllPlayerInfoUpdateThread(String firstPlayerName) {
    return new Thread(() -> {
      GameRequestSender gameRequestSender = App.getGameRequestSender();
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
            setupAllPlayerInfoGui(0, firstPlayerName);
            isFirstCheck = false;
          } else {
            // not first check, updating the player info gui
            for (PlayerInGame playerInfo : playerStates.getPlayersInfo().values()) {
              //// if we are updating the btm player info, buttons need to be re-assign actions
              //if (playerInfo.getName().equals(App.getUser().getUsername())) {
              //  myCardButton.setOnAction(createOpenMyPurchaseCardClick());
              //  myReservedCardsButton.setOnAction(createOpenMyReserveCardClick());
              //}
              updatePlayerInfoGui(playerInfo);
            }
          }
        }
      }
    });
  }


  // For setup initial playerInfoGuis to display for the first time
  private void setupAllPlayerInfoGui(int initTokenAmount, String firstPlayer) {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    User curUser = App.getUser();

    int playerCount = sortedPlayerNames.size();
    PlayerPosition[] playerPositions = PlayerPosition.values();
    // iterate through the players we have (sorted, add their GUI accordingly)
    for (int i = 0; i < playerCount; i++) {
      PlayerPosition position = playerPositions[i];
      // horizontal player GUI setup
      if (position.equals(PlayerPosition.BOTTOM) || position.equals(PlayerPosition.TOP)) {
        // decide player names based on player position (sorted above)
        String playerName;
        if (position.equals(PlayerPosition.BOTTOM)) {
          playerName = curUser.getUsername();
          // allow user to click on my cards/reserved cards
          myCardButton.setOnAction(createOpenMyPurchaseCardClick());
          myReservedCardsButton.setOnAction(createOpenMyReserveCardClick());
        } else {
          playerName = sortedPlayerNames.get(2);
        }

        int armCode;
        if (nameToArmCodeMap.isEmpty()) {
          armCode = -1;
        } else {
          armCode = nameToArmCodeMap.get(playerName);
        }
        // initialize with diff arm code depends on existence of trader extension
        HorizontalPlayerInfoGui horizontalPlayerInfoGui = new HorizontalPlayerInfoGui(
            position,
            playerName,
            initTokenAmount,
            armCode);
        // set up GUI layout X and Y
        if (position.equals(PlayerPosition.BOTTOM)) {
          horizontalPlayerInfoGui.setup(config.getBtmPlayerLayoutX(), config.getBtmPlayerLayoutY());
        } else {
          horizontalPlayerInfoGui.setup(config.getTopPlayerLayoutX(), config.getTopPlayerLayoutY());
        }
        // add to map, so it's easier to get them afterwards
        nameToPlayerInfoGuiMap.put(playerName, horizontalPlayerInfoGui);
      }

      // identical logic with horizontal players GUI
      if (position.equals(PlayerPosition.LEFT) || position.equals(PlayerPosition.RIGHT)) {
        String playerName;
        if (position.equals(PlayerPosition.LEFT)) {
          playerName = sortedPlayerNames.get(1);
        } else {
          playerName = sortedPlayerNames.get(3);
        }

        int armCode;
        if (nameToArmCodeMap.isEmpty()) {
          armCode = -1;
        } else {
          armCode = nameToArmCodeMap.get(playerName);
        }
        VerticalPlayerInfoGui verticalPlayerInfoGui = new VerticalPlayerInfoGui(
            position,
            playerName,
            initTokenAmount,
            armCode);
        if (position.equals(PlayerPosition.LEFT)) {
          verticalPlayerInfoGui.setup(
              config.getLeftPlayerLayoutX(),
              config.getLeftPlayerLayoutY()
          );
        } else {
          verticalPlayerInfoGui.setup(
              config.getRightPlayerLayoutX(),
              config.getRightPlayerLayoutY()
          );
        }
        nameToPlayerInfoGuiMap.put(playerName, verticalPlayerInfoGui);
      }
    }

    // now the gui is set, postpone displaying to main thread
    Platform.runLater(() -> {
      for (PlayerInfoGui playerInfoGui : nameToPlayerInfoGuiMap.values()) {
        if (playerInfoGui instanceof VerticalPlayerInfoGui) {
          playerBoardAnchorPane.getChildren().add((VerticalPlayerInfoGui) playerInfoGui);
        }
        if (playerInfoGui instanceof HorizontalPlayerInfoGui) {
          playerBoardAnchorPane.getChildren().add((HorizontalPlayerInfoGui) playerInfoGui);
        }
      }
    });
    // since it's in the first check, curTurn and firstPlayer are same,
    // highlight first player
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
  private Thread generateGameInfoUpdateThread() {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    GameRequestSender gameRequestSender = App.getGameRequestSender();
    User curUser = App.getUser(); // at this point, user will not be Null
    return new Thread(() -> {
      // basic stuff needed for a long pull
      String hashedResponse = "";
      HttpResponse<String> longPullResponse = null;
      boolean isFirstCheck = true;

      while (true) {
        // always do one, just in case
        // after this, curUser.getAccessToken() will for sure have a valid token
        App.refreshUserToken(curUser);

        int responseCode = 408;
        while (responseCode == 408) {
          longPullResponse = gameRequestSender.sendGetGameInfoRequest(gameId, hashedResponse);
          responseCode = longPullResponse.getStatus();
        }

        if (responseCode == 200) {
          // update the MD5 hash of previous response
          hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
          // decode this response into GameInfo class with Gson
          String responseInJsonString = longPullResponse.getBody();
          Gson gsonParser = SplendorDevHelper.getInstance().getGson();
          GameInfo curGameInfo = gsonParser.fromJson(responseInJsonString, GameInfo.class);
          //TODO: For this game application, we always play BASE + ORIENT, thus
          // we do not worry about NOT having their GUI set up

          // if isFirstCheck = True (setting up stage)
          // Step 1. setup base board gui

          // Step 2. setup orient board gui

          // Step 3. (optionally) setup extension board gui


          // if isFirstCheck = False (update stage)
          // Step 1. update base board gui

          // Step 2. update orient board gui

          // Step 3. (optionally) update extension board gui

          // First, check what extensions are we playing
          List<Extension> extensions = curGameInfo.getExtensions();
          TableTop tableTop = curGameInfo.getTableTop();
          String playerName = curUser.getUsername();
          String accessToken = curUser.getAccessToken();
          String actionMapJson = gameRequestSender
              .sendGetInitialPlayerActions(gameId, playerName, accessToken).getBody();
          Type actionMapType = new TypeToken<Map<String, Action>>() {}.getType();
          Map<String, Action> playerActionMap = gsonParser.fromJson(actionMapJson, actionMapType);
          if (isFirstCheck) {
            // generate BoardGui based on extension type
            for (Extension extension : extensions) {
              switch (extension) {
                case BASE:
                  BaseBoardGui baseBoardGui = new BaseBoardGui(
                      playerBoardAnchorPane,
                      playerActionMap,
                      gameId);
                  baseBoardGui.guiSetup(tableTop);
                  extensionBoardGuiMap.put(extension, baseBoardGui);
                  break;
                case ORIENT:
                  extensionBoardGuiMap.put(extension, new OrientBoardGui());
                  break;
                case TRADING_POST:
                  extensionBoardGuiMap.put(extension, new TraderBoardGui());
                  break;
                case CITY:
                  extensionBoardGuiMap.put(extension, new CityBoardGui());
                  break;
                default: break;
              }
            }



          } else {
            // make use of extensionBoardGuiMap and do updates
          }






          //if (isFirstCheck) {
          //  // TODO: Potential nested clickable noble cards
          //  // initialize noble area
          //  nobleBoard = new NobleBoardGui(100, 100, 5);
          //  List<NobleCard> nobles = curGameInfo.getTableTop().getNobles();
          //  Platform.runLater(() -> {
          //    nobleBoard.setup(nobles, config.getNobleLayoutX(), config.getNobleLayoutY(), true);
          //    playerBoardAnchorPane.getChildren().add(nobleBoard);
          //  });
          //
          //  // initialize token area
          //  tokenBankGui = new TokenBankGui();
          //  EnumMap<Colour, Integer> bankBalance =
          //      curGameInfo.getTableTop().getBank().getAllTokens();
          //  Platform.runLater(() -> {
          //    tokenBankGui.setup(bankBalance,
          //        config.getTokenBankLayoutX(),
          //        config.getTokenBankLayoutY(), true);
          //    playerBoardAnchorPane.getChildren().add(tokenBankGui);
          //  });
          //
          //  // initialize the base board
          //  for (int i = 3; i >= 1; i--) {
          //    List<BaseCard> oneLevelCards =
          //        curGameInfo.getTableTop().getBaseBoard().getBaseCardsOnBoard().get(i);
          //    List<BaseCard> oneLevelDeck =
          //        curGameInfo.getTableTop().getBaseBoard().getBaseDecks().get(i);
          //    BaseCardLevelGui baseCardLevelGui =
          //        new BaseCardLevelGui(i, oneLevelCards, oneLevelDeck);
          //    baseCardLevelGui.setup();
          //    Platform.runLater(() -> {
          //      baseCardBoard.getChildren().add(baseCardLevelGui);
          //    });
          //    baseCardGuiMap.put(i, baseCardLevelGui);
          //  }
          //  try {
          //    System.out.println(
          //        "First time generate actions for player: " + curUser.getUsername());
          //    assignActionsToCardBoard();
          //  } catch (UnirestException e) {
          //    throw new RuntimeException(e);
          //  }
          //  isFirstCheck = false;
          //} else { // If it is not first check.....
          //  // need to display
          //  // TODO:
          //  //  1. NEW Cards on board (and their actions)
          //  //  2. New Nobles on board (and their actions) DONE
          //  //  3. New token bank info (and action?) DONE
          //
          //  // First step, change the highlight (if prePlayer and curren player does not match)
          //  String currentPlayerName = curGameInfo.getCurrentPlayer();
          //  if (!prePlayerName.equals(currentPlayerName)) {
          //    nameToPlayerInfoGuiMap.get(prePlayerName).setHighlight(false);
          //    nameToPlayerInfoGuiMap.get(currentPlayerName).setHighlight(true);
          //    prePlayerName = currentPlayerName;
          //  } // if they are the same, no need to change the height colour
          //
          //  List<NobleCard> nobles = curGameInfo.getTableTop().getNobles();
          //  Platform.runLater(() -> {
          //    nobleBoard.setup(nobles,
          //        config.getNobleLayoutX(),
          //        config.getNobleLayoutY(), false);
          //  });
          //
          //  EnumMap<Colour, Integer> bankBalance =
          //      curGameInfo.getTableTop().getBank().getAllTokens();
          //  Platform.runLater(() -> {
          //    tokenBankGui.setup(bankBalance,
          //        config.getTokenBankLayoutX(),
          //        config.getTokenBankLayoutY(), false);
          //  });
          //
          //  // update the cards GUI
          //  for (int i = 3; i >= 1; i--) {
          //    BaseCardLevelGui oneLevelCardsGui = baseCardGuiMap.get(i);
          //    List<BaseCard> oneLevelCards =
          //        curGameInfo.getTableTop().getBaseBoard().getBaseCardsOnBoard().get(i);
          //    List<BaseCard> oneLevelDeck =
          //        curGameInfo.getTableTop().getBaseBoard().getBaseDecks().get(i);
          //    oneLevelCardsGui.setCards(oneLevelCards);
          //    oneLevelCardsGui.setDeck(oneLevelDeck);
          //    oneLevelCardsGui.setup();
          //  }
          //  try {
          //    System.out.println("Updating actions for player: " + curUser.getUsername());
          //    assignActionsToCardBoard();
          //  } catch (UnirestException e) {
          //    throw new RuntimeException(e);
          //  }
          //}
        }
      }
    });
  }

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
    List<String> playerNames = curGameInfo.getPlayerNames();
    // sort the player names and store it to this game controller
    if (sortedPlayerNames.isEmpty()) {
      sortedPlayerNames = sortPlayerNames(App.getUser().getUsername(), playerNames);
    }

    // if we are playing the Trading Extension, initialize the map of player name
    // to their arm code index
    List<Extension> extensionsPlaying = curGameInfo.getExtensions();
    if (extensionsPlaying.contains(Extension.TRADING_POST)) {
      for (int i = 1; i <= playerNames.size(); i++) {
        nameToArmCodeMap.put(playerNames.get(i-1), i);
      }
    }
    String firstPlayerName = curGameInfo.getFirstPlayerName();
    Thread playerInfoThread = generateAllPlayerInfoUpdateThread(firstPlayerName);
    playerInfoThread.start();
    Thread mainGameUpdateThread = generateGameInfoUpdateThread();
    mainGameUpdateThread.start();
    //// start the thread for main board and playerInfo update at the same time



  }
}
