package project.controllers.stagecontrollers;


import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.CardExtraAction;
import ca.mcgill.comp361.splendormodel.model.CardEffect;
import ca.mcgill.comp361.splendormodel.model.Colour;
import ca.mcgill.comp361.splendormodel.model.Extension;
import ca.mcgill.comp361.splendormodel.model.GameInfo;
import ca.mcgill.comp361.splendormodel.model.PlayerInGame;
import ca.mcgill.comp361.splendormodel.model.PlayerStates;
import ca.mcgill.comp361.splendormodel.model.PurchasedHand;
import ca.mcgill.comp361.splendormodel.model.ReservedHand;
import ca.mcgill.comp361.splendormodel.model.SplendorDevHelper;
import ca.mcgill.comp361.splendormodel.model.TableTop;
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
import javafx.scene.shape.Rectangle;
import org.apache.commons.codec.digest.DigestUtils;
import project.App;
import project.GameBoardLayoutConfig;
import project.connection.GameRequestSender;
import project.controllers.popupcontrollers.GameOverPopUpController;
import project.controllers.popupcontrollers.PurchaseHandController;
import project.controllers.popupcontrollers.ReservedHandController;
import project.controllers.popupcontrollers.SaveGamePopUpController;
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

/**
 * Game controller for game GUI.
 */
public class GameController implements Initializable {


  private final Rectangle coverRectangle = new Rectangle(
      App.getGuiLayouts().getAppWidth(),
      App.getGuiLayouts().getAppHeight());
  private final long gameId;
  private final Session curSession;
  private final Map<Integer, BaseCardLevelGui> baseCardGuiMap = new HashMap<>();
  private final Map<String, PlayerInfoGui> nameToPlayerInfoGuiMap = new HashMap<>();
  private final Map<String, Integer> nameToArmCodeMap = new HashMap<>();
  private final Map<Extension, BoardGui> extensionBoardGuiMap = new HashMap<>();
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
  private Button saveButton;
  @FXML
  private Button myReservedCardsButton;
  @FXML
  private Button quitButton;
  private String lastTurnPlayerName;
  private NobleBoardGui nobleBoard;
  private TokenBankGui tokenBankGui;
  private List<String> sortedPlayerNames = new ArrayList<>();
  private Thread playerInfoThread;
  private Thread mainGameUpdateThread;

  /**
   * GameController for the main page.
   *
   * @param gameId gameId
   * @param curSession curSession
   */
  public GameController(long gameId, Session curSession) {
    this.gameId = gameId;
    this.curSession = curSession;
    this.playerInfoThread = null;
    this.mainGameUpdateThread = null;
  }


  /**
   * Opening the development cards pop up once "My Cards" button is pressed.
   */

  //@FXML
  //protected void onExitGameClick() throws IOException {
  //  App.setRoot("admin_lobby_page");
  //}
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
      String gameInfoJson;
      try {
        gameInfoJson = sender.sendGetGameInfoRequest(gameId, "").getBody();
      } catch (UnirestException e) {
        throw new RuntimeException(e);
      }
      GameInfo gameInfo = gsonParser.fromJson(gameInfoJson, GameInfo.class);
      String playerName = App.getUser().getUsername();
      Map<String, Action> playerActions = gameInfo.getPlayerActionMaps().get(playerName);

      try {
        App.loadPopUpWithController("my_reserved_cards.fxml",
            new ReservedHandController(reservedHand, playerActions, coverRectangle, gameId),
            coverRectangle, 800, 600);

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
      String gameInfoJson;
      try {
        gameInfoJson = sender.sendGetGameInfoRequest(gameId, "").getBody();
      } catch (UnirestException e) {
        throw new RuntimeException(e);
      }
      GameInfo gameInfo = gsonParser.fromJson(gameInfoJson, GameInfo.class);
      String playerName = App.getUser().getUsername();
      Map<String, Action> playerActions = gameInfo.getPlayerActionMaps().get(playerName);

      try {
        App.loadPopUpWithController("my_development_cards.fxml",
            new PurchaseHandController(gameId, purchasedHand, playerActions, coverRectangle),
            coverRectangle,
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

  //private Map<PlayerPosition, String> setPlayerToPosition(String curPlayerName,
  //                                                        List<String> allPlayerNames) {
  //  Map<PlayerPosition, String> resultMap = new HashMap<>();
  //  List<String> orderedNames = sortPlayerNames(curPlayerName, allPlayerNames);
  //  for (int i = 0; i < orderedNames.size(); i++) {
  //    resultMap.put(PlayerPosition.values()[i], orderedNames.get(i));
  //  }
  //  return resultMap;
  //}


  /**
   * Update one PlayerInfoGui based on one PlayerInGame object.
   *
   * @param curPlayerInGame curPlayerInGame
   */
  private void updatePlayerInfoGui(PlayerInGame curPlayerInGame) {

    String playerName = curPlayerInGame.getName();
    int newPoints = curPlayerInGame.getPrestigePoints();
    EnumMap<Colour, Integer> newTokenInHand = curPlayerInGame.getTokenHand().getAllTokens();
    // this gems contain gold colour orient card count!!!!
    EnumMap<Colour, Integer> gemsInHand = curPlayerInGame.getTotalGems();
    // Get the player gui
    PlayerInfoGui playerInfoGui = nameToPlayerInfoGuiMap.get(playerName);
    // updating the GUI based on the new info from server
    Platform.runLater(() -> {
      // update the public-player associated area
      // TODO: Add updating number of noble reserved and number of dev cards reserved
      playerInfoGui.setNewPrestigePoints(newPoints);
      playerInfoGui.setNewTokenInHand(newTokenInHand);
      System.out.println(App.getUser().getUsername() + " has tokens in hand: " + newTokenInHand);
      System.out.println(App.getUser().getUsername() + " has gems in hand: " + gemsInHand);
      playerInfoGui.setGemsInHand(gemsInHand);
    });
  }


  private Thread generateAllPlayerInfoUpdateThread() {
    return new Thread(() -> {
      GameRequestSender gameRequestSender = App.getGameRequestSender();
      String hashedResponse = "";
      HttpResponse<String> longPullResponse = null;
      try {
        while (!Thread.currentThread().isInterrupted()) {
          int responseCode = 408;
          while (responseCode == 408) {
            longPullResponse =
                gameRequestSender.sendGetAllPlayerInfoRequest(gameId, hashedResponse);
            responseCode = longPullResponse.getStatus();
          }

          if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("PlayerInfo update thread interrupted");
          }

          if (responseCode == 200) {
            hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
            // decode this response into PlayerInGame class with Gson
            String responseInJsonString = longPullResponse.getBody();
            Gson splendorParser = SplendorDevHelper.getInstance().getGson();
            PlayerStates playerStates =
                splendorParser.fromJson(responseInJsonString, PlayerStates.class);
            // set up GUI
            setupAllPlayerInfoGui(0);

            // update information on the GUI
            for (PlayerInGame playerInfo : playerStates.getPlayersInfo().values()) {
              updatePlayerInfoGui(playerInfo);
            }

          }
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
      }

    });
  }


  // For setup initial playerInfoGuis to display for the first time
  private void setupAllPlayerInfoGui(int initTokenAmount) {
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
    //// at this point, the map can not be empty, safely get the playerGui
    //nameToPlayerInfoGuiMap.get(firstPlayer).setHighlight(true);
  }

  //
  private Thread generateGameInfoUpdateThread() {
    GameRequestSender gameRequestSender = App.getGameRequestSender();
    User curUser = App.getUser(); // at this point, user will not be Null
    return new Thread(() -> {
      // basic stuff needed for a long pull
      String hashedResponse = "";
      HttpResponse<String> longPullResponse = null;
      try {
        while (!Thread.currentThread().isInterrupted()) {
          // always do one, just in case
          // after this, curUser.getAccessToken() will for sure have a valid token
          App.refreshUserToken(curUser);
          int responseCode = 408;
          while (responseCode == 408) {
            longPullResponse = gameRequestSender.sendGetGameInfoRequest(gameId, hashedResponse);
            responseCode = longPullResponse.getStatus();
          }
          if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("game info update thread is interrupted");
          }

          if (responseCode == 200) {
            // update the MD5 hash of previous response
            hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
            // decode this response into GameInfo class with Gson
            String responseInJsonString = longPullResponse.getBody();
            Gson gsonParser = SplendorDevHelper.getInstance().getGson();
            GameInfo curGameInfo = gsonParser.fromJson(responseInJsonString, GameInfo.class);

            // the current game is finished (either done by Save OR GameOver)
            if (curGameInfo.isFinished()) {
              // should load a game over page (jump back to lobby after they click the button)
              // implicitly handle the threading stopping logic and loading back to lobby
              Platform.runLater(() -> {
                try {
                  App.loadPopUpWithController("game_over.fxml",
                      new GameOverPopUpController(mainGameUpdateThread, playerInfoThread),
                      coverRectangle, 360, 170);
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              });
            }
            //TODO: For this game application, we always play BASE + ORIENT, thus
            // we do not worry about NOT having their GUI set up

            // Step 0. who's turn is it update (highlight feature)

            // Step 1. update base board gui

            // Step 2. update orient board gui

            // Step 3. (optionally) update extension board gui

            // TODO: Step 4. update MyPurchaseHand and MyReserveHand


            // First, check what extensions are we playing
            List<Extension> extensions = curGameInfo.getExtensions();
            TableTop tableTop = curGameInfo.getTableTop();
            // always get the action map from game info
            String playerName = curUser.getUsername();
            Map<String, Action> playerActionMap = curGameInfo.getPlayerActionMaps().get(playerName);
            boolean allPairActions = playerActionMap.values().stream()
                .allMatch(action -> action instanceof CardExtraAction &&
                    ((CardExtraAction) action).getCardEffect().equals(CardEffect.SATCHEL));

            if (!playerActionMap.isEmpty() && allPairActions) {
              HttpResponse<String> response =
                  gameRequestSender.sendGetAllPlayerInfoRequest(gameId, "");
              String playerStatesJson = response.getBody();
              PlayerStates playerStates = SplendorDevHelper.getInstance().getGson()
                  .fromJson(playerStatesJson, PlayerStates.class);
              PlayerInGame playerInGame = playerStates.getOnePlayerInGame(playerName);
              PurchasedHand purchasedHand = playerInGame.getPurchasedHand();
              Platform.runLater(() -> {
                try {
                  App.loadPopUpWithController("my_development_cards.fxml",
                      new PurchaseHandController(gameId, purchasedHand, playerActionMap, coverRectangle),
                      coverRectangle,
                      800,
                      600);
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              });
            }

            // TODO: After one purchase, the server side did not set next player properly
            System.out.println("Current turn: " + curGameInfo.getCurrentPlayer());
            System.out.println("Player: " + playerName + playerActionMap.values());
            // clear up all children in playerBoardAnchorPane
            for (BoardGui boardGui : extensionBoardGuiMap.values()) {
              Platform.runLater(boardGui::clearContent);
            }

            // generate BoardGui based on extension type
            for (Extension extension : extensions) {
              switch (extension) {
                case BASE:
                  BaseBoardGui baseBoardGui = new BaseBoardGui(playerBoardAnchorPane,
                      gameId, coverRectangle);
                  baseBoardGui.initialGuiActionSetup(tableTop, playerActionMap);
                  extensionBoardGuiMap.put(extension, baseBoardGui);
                  break;
                case ORIENT:
                  OrientBoardGui orientBoardGui = new OrientBoardGui(playerBoardAnchorPane,
                      gameId, coverRectangle);
                  orientBoardGui.initialGuiActionSetup(tableTop, playerActionMap);
                  extensionBoardGuiMap.put(extension, orientBoardGui);
                  break;
                case TRADING_POST:
                  GameBoardLayoutConfig config = App.getGuiLayouts();
                  TraderBoardGui traderBoardGui = new TraderBoardGui(gameId, nameToArmCodeMap);
                  traderBoardGui.initialGuiActionSetup(tableTop, playerActionMap);
                  traderBoardGui.setLayoutX(config.getPacBoardLayoutX());
                  traderBoardGui.setLayoutY(config.getPacBoardLayoutY());
                  Platform.runLater(() -> {
                    playerBoardAnchorPane.getChildren().add(traderBoardGui);
                  });
                  extensionBoardGuiMap.put(extension, traderBoardGui);
                  break;
                case CITY:
                  System.out.println("\nThis is city DLC\n");
                  CityBoardGui cityBoardGui = new CityBoardGui(playerBoardAnchorPane,
                      gameId, coverRectangle);
                  cityBoardGui.initialGuiActionSetup(tableTop, playerActionMap);
                  extensionBoardGuiMap.put(extension, cityBoardGui);
                  break;
                default: break;
              }
            }

            // highlight players accordingly
            String curTurnPlayerName = curGameInfo.getCurrentPlayer();
            if (!nameToPlayerInfoGuiMap.isEmpty()) {
              for (String name : nameToPlayerInfoGuiMap.keySet()) {
                nameToPlayerInfoGuiMap.get(name).setHighlight(name.equals(curTurnPlayerName));
              }
            }
          }
        }
      } catch (InterruptedException | UnirestException e) {
        System.out.println(e.getMessage());
      }

    });
  }

  // interrupt the game update thread to save resources
  private EventHandler<ActionEvent> createClickOnSaveButtonEvent(GameInfo gameInfo, long gameId) {
    return event -> {
      try {
        App.loadPopUpWithController("save_game.fxml",
            new SaveGamePopUpController(gameInfo, gameId, playerInfoThread, mainGameUpdateThread),
            coverRectangle,
            360,
            170);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }

  // interrupt the game update thread to save resources
  private EventHandler<ActionEvent> createClickOnQuitButtonEvent() {
    return event -> {
      try {
        App.loadPopUpWithController("quit_game.fxml",
            new GameOverPopUpController(mainGameUpdateThread, playerInfoThread),
            coverRectangle, 360, 170);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }

  @Override
  // TODO: This method contains what's gonna happen after clicking "play" on the board
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // the shaded shape that is used to prevent player to mess around with
    // actions when we do not allow them to click on things that are
    // not in pop up window

    GameRequestSender gameRequestSender = App.getGameRequestSender();
    HttpResponse<String> firstGameInfoResponse = null;
    try {
      firstGameInfoResponse = gameRequestSender.sendGetGameInfoRequest(gameId, "");
    } catch (UnirestException e) {
      System.out.println(e.getMessage());
    }
    Gson gsonParser = SplendorDevHelper.getInstance().getGson();
    GameInfo curGameInfo = gsonParser.fromJson(firstGameInfoResponse.getBody(), GameInfo.class);
    //lastTurnPlayerName = curGameInfo.getFirstPlayerName();
    saveButton.setDisable(true);
    quitButton.setOnAction(createClickOnQuitButtonEvent());
    if (App.getUser().getUsername().equals(curGameInfo.getCreator())) {
      // if the current user is the creator, activate the save button, otherwise it
      // greyed out
      saveButton.setDisable(false);
      saveButton.setOnAction(createClickOnSaveButtonEvent(curGameInfo, gameId));
    }

    List<String> playerNames = curGameInfo.getPlayerNames();
    // sort the player names and store it to this game controller
    if (sortedPlayerNames.isEmpty()) {
      sortedPlayerNames = sortPlayerNames(App.getUser().getUsername(), playerNames);
    }

    // if we are playing the Trading Extension, initialize the map of player name
    // to their arm code index
    List<Extension> extensionsPlaying = curGameInfo.getExtensions();
    playerNames = curGameInfo.getPlayerNames();
    if (extensionsPlaying.contains(Extension.TRADING_POST)) {
      for (int i = 1; i <= playerNames.size(); i++) {
        nameToArmCodeMap.put(playerNames.get(i - 1), i);
      }
    }

    playerInfoThread = generateAllPlayerInfoUpdateThread();
    playerInfoThread.start();

    mainGameUpdateThread = generateGameInfoUpdateThread();
    mainGameUpdateThread.start();

  }
}
