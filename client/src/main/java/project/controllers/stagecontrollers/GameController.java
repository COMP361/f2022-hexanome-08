package project.controllers.stagecontrollers;


import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.BonusTokenPowerAction;
import ca.mcgill.comp361.splendormodel.actions.CardExtraAction;
import ca.mcgill.comp361.splendormodel.actions.ClaimCityAction;
import ca.mcgill.comp361.splendormodel.actions.ClaimNobleAction;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.commons.codec.digest.DigestUtils;
import project.App;
import project.config.GameBoardLayoutConfig;
import project.connection.GameRequestSender;
import project.controllers.popupcontrollers.ActOnNoblePopUpController;
import project.controllers.popupcontrollers.BonusTokenPopUpController;
import project.controllers.popupcontrollers.BurnCardController;
import project.controllers.popupcontrollers.ClaimCityPopUpController;
import project.controllers.popupcontrollers.FreeCardPopUpController;
import project.controllers.popupcontrollers.GameOverPopUpController;
import project.controllers.popupcontrollers.PurchaseHandController;
import project.controllers.popupcontrollers.ReservedHandController;
import project.controllers.popupcontrollers.SaveGamePopUpController;
import project.view.splendor.boardgui.BaseBoardGui;
import project.view.splendor.boardgui.BoardGui;
import project.view.splendor.boardgui.CityBoardGui;
import project.view.splendor.boardgui.NobleBoardGui;
import project.view.splendor.boardgui.OrientBoardGui;
import project.view.splendor.boardgui.TokenBankGui;
import project.view.splendor.boardgui.TraderBoardGui;
import project.view.splendor.playergui.PlayerInfoGui;
import project.view.splendor.playergui.PlayerPosition;

/**
 * Game controller for game GUI.
 */
public class GameController implements Initializable {

  private final long gameId;
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
  private Button pendingActionButton;

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

  private String viewerName = null;

  private boolean inWatchMode = false;

  /**
   * GameController for the main page.
   *
   * @param gameId gameId
   */
  public GameController(long gameId, String viewerName) {
    this.gameId = gameId;
    // set up for indicating this is a watch game or not
    if (viewerName != null) {
      this.viewerName = viewerName;
    } else {
      inWatchMode = true;
    }
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
    GameBoardLayoutConfig config = App.getGuiLayouts();
    return event -> {
      GameRequestSender sender = App.getGameRequestSender();
      String curPlayerName = viewerName;
      String playerStatsJson = sender.sendGetAllPlayerInfoRequest(gameId, "").getBody();
      Gson gsonParser = SplendorDevHelper.getInstance().getGson();
      PlayerStates playerStates = gsonParser.fromJson(playerStatsJson, PlayerStates.class);
      // every time button click, we have up-to-date information
      PlayerInGame playerInGame = playerStates.getOnePlayerInGame(curPlayerName);
      ReservedHand reservedHand = playerInGame.getReservedHand();
      String gameInfoJson = sender.sendGetGameInfoRequest(gameId, "").getBody();
      GameInfo gameInfo = gsonParser.fromJson(gameInfoJson, GameInfo.class);
      String playerName = viewerName;
      Map<String, Action> playerActions = gameInfo.getPlayerActionMaps().get(playerName);

      App.loadPopUpWithController("my_reserved_cards.fxml",
          new ReservedHandController(reservedHand, playerActions, gameId),
          config.getLargePopUpWidth(),
          config.getLargePopUpHeight());

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
    GameBoardLayoutConfig config = App.getGuiLayouts();
    return event -> {
      GameRequestSender sender = App.getGameRequestSender();
      String curPlayerName = viewerName;
      String playerStatsJson = sender.sendGetAllPlayerInfoRequest(gameId, "").getBody();
      Gson gsonParser = SplendorDevHelper.getInstance().getGson();
      PlayerStates playerStates = gsonParser.fromJson(playerStatsJson, PlayerStates.class);
      // every time button click, we have up-to-date information
      PlayerInGame playerInGame = playerStates.getOnePlayerInGame(curPlayerName);
      String gameInfoJson = sender.sendGetGameInfoRequest(gameId, "").getBody();
      GameInfo gameInfo = gsonParser.fromJson(gameInfoJson, GameInfo.class);
      String playerName = viewerName;
      Map<String, Action> playerActions = gameInfo.getPlayerActionMaps().get(playerName);

      App.loadPopUpWithController("my_development_cards.fxml",
          new PurchaseHandController(gameId, playerInGame, playerActions),
          config.getLargePopUpWidth(),
          config.getLargePopUpHeight());
    };
  }


  private void clearAllPlayerInfoGui() {
    if (!nameToPlayerInfoGuiMap.isEmpty()) {
      for (PlayerInfoGui playerInfoGui : nameToPlayerInfoGuiMap.values()) {
        Platform.runLater(() -> {
          ObservableList<Node> mainBoardChildren = playerBoardAnchorPane.getChildren();
          mainBoardChildren.remove(playerInfoGui.getContainer());
        });
      }
      // clean the map
      nameToPlayerInfoGuiMap.clear();
    }
  }


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
    // get number of reserved cards
    int numOfReservedCards = curPlayerInGame.getReservedHand().getDevelopmentCards().size();
    int numOfReservedNobles = curPlayerInGame.getReservedHand().getNobleCards().size();
    // Get the player gui
    PlayerInfoGui playerInfoGui = nameToPlayerInfoGuiMap.get(playerName);
    // updating the GUI based on the new info from server
    Platform.runLater(() -> {
      // update the public-player associated area
      playerInfoGui.setNewPrestigePoints(newPoints);
      playerInfoGui.setNewTokenInHand(newTokenInHand);
      playerInfoGui.setGemsInHand(gemsInHand);
      playerInfoGui.setReservedCardCount(numOfReservedCards);
      playerInfoGui.setReservedNobleCount(numOfReservedNobles);
    });
  }


  private Thread generateAllPlayerInfoUpdateThread() {
    return new Thread(() -> {
      GameRequestSender gameRequestSender = App.getGameRequestSender();
      String hashedResponse = "";
      HttpResponse<String> longPullResponse = null;

      while (!Thread.currentThread().isInterrupted()) {
        try {
          int responseCode = 408;
          while (responseCode == 408) {
            longPullResponse =
                gameRequestSender.sendGetAllPlayerInfoRequest(gameId, hashedResponse);
            responseCode = longPullResponse.getStatus();
            if (Thread.currentThread().isInterrupted()) {
              throw new InterruptedException(
                  "PlayerInfo Thread: " + Thread.currentThread().getName() +
                      " terminated");
            }
          }

          if (responseCode == 200) {
            hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
            // decode this response into PlayerInGame class with Gson
            String responseInJsonString = longPullResponse.getBody();
            Gson splendorParser = SplendorDevHelper.getInstance().getGson();
            PlayerStates playerStates =
                splendorParser.fromJson(responseInJsonString, PlayerStates.class);

            // clear the previous GUI
            clearAllPlayerInfoGui();

            // set up GUI
            setupAllPlayerInfoGui();

            // update information on the GUI
            for (PlayerInGame playerInfo : playerStates.getPlayersInfo().values()) {
              updatePlayerInfoGui(playerInfo);
            }

          }

        } catch (InterruptedException e) {
          System.out.println(Thread.currentThread().getName() + " is dead!");
          break;
        }

      }

    });
  }


  // For setup initial playerInfoGuis to display for the first time
  private void setupAllPlayerInfoGui() {
    PlayerPosition[] playerPositions = PlayerPosition.values();
    // iterate through the players we have (sorted, add their GUI accordingly)
    for (int positionIndex = 0; positionIndex < sortedPlayerNames.size(); positionIndex++) {
      PlayerPosition position = playerPositions[positionIndex];
      String playerName;
      if (position == PlayerPosition.BOTTOM) {
        playerName = viewerName;
        if (!inWatchMode) {
          // allow user to click on my cards/reserved cards
          // if not in watch mode
          myCardButton.setOnAction(createOpenMyPurchaseCardClick());
          myReservedCardsButton.setOnAction(createOpenMyReserveCardClick());
        }
      } else {
        playerName = sortedPlayerNames.get(positionIndex);
      }
      int armCode;
      if (nameToArmCodeMap.isEmpty()) {
        armCode = -1;
      } else {
        armCode = nameToArmCodeMap.get(playerName);
      }
      PlayerInfoGui playerInfoGui = new PlayerInfoGui(gameId, position, playerName, armCode);
      // set up based on position
      switch (position) {
        case BOTTOM:
          playerInfoGui.setup(
              App.getGuiLayouts().getBtmPlayerLayoutX(),
              App.getGuiLayouts().getBtmPlayerLayoutY());
          break;

        case TOP:
          playerInfoGui.setup(
              App.getGuiLayouts().getTopPlayerLayoutX(),
              App.getGuiLayouts().getTopPlayerLayoutY());
          break;
        case LEFT:
          playerInfoGui.setup(
              App.getGuiLayouts().getLeftPlayerLayoutX(),
              App.getGuiLayouts().getLeftPlayerLayoutY());
          break;
        case RIGHT:
          playerInfoGui.setup(
              App.getGuiLayouts().getRightPlayerLayoutX(),
              App.getGuiLayouts().getRightPlayerLayoutY());
          break;

        default:break;
      }
      nameToPlayerInfoGuiMap.put(playerName, playerInfoGui);
    }
    // now the gui is set, postpone displaying to main thread
    Platform.runLater(() -> {
      for (PlayerInfoGui playerInfoGui : nameToPlayerInfoGuiMap.values()) {
        playerBoardAnchorPane.getChildren().add(playerInfoGui.getContainer());
      }
    });
  }

  private void showClaimNoblePopUp(GameInfo curGameInfo) {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    Map<String, Action> playerActionMap = curGameInfo.getPlayerActionMaps().get(viewerName);
    // return true if EVERY ACTION in playerActionMap is ClaimNobleAction
    boolean allClaimNobleActions = playerActionMap.values().stream()
        .allMatch(action -> action instanceof ClaimNobleAction);

    if (!playerActionMap.isEmpty() && allClaimNobleActions) {
      // enable player to continue their pending action even they close the window
      pendingActionButton.setDisable(false);
      pendingActionButton.setOnAction(event -> {
        Platform.runLater(() -> {
          App.loadPopUpWithController("noble_action_pop_up.fxml",
              new ActOnNoblePopUpController(gameId, playerActionMap, false),
              config.getSmallPopUpWidth(),
              config.getSmallPopUpHeight());
        });
      });
    }
  }

  private void showPairingCardPopUp(GameInfo curGameInfo) {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    // generate special pop up for pairing card
    Map<String, Action> playerActionMap = curGameInfo.getPlayerActionMaps().get(viewerName);
    boolean allPairActions = playerActionMap.values().stream()
        .allMatch(action -> action instanceof CardExtraAction
            && ((CardExtraAction) action).getCardEffect().equals(CardEffect.SATCHEL));
    if (!playerActionMap.isEmpty() && allPairActions) {
      HttpResponse<String> response =
          App.getGameRequestSender().sendGetAllPlayerInfoRequest(gameId, "");
      String playerStatesJson = response.getBody();
      PlayerStates playerStates = SplendorDevHelper.getInstance().getGson()
          .fromJson(playerStatesJson, PlayerStates.class);
      PlayerInGame playerInGame = playerStates.getOnePlayerInGame(viewerName);
      // also assign the pending action button some functionality
      pendingActionButton.setDisable(false);
      pendingActionButton.setOnAction(event -> {
        Platform.runLater(() -> {
          App.loadPopUpWithController("my_development_cards.fxml",
              new PurchaseHandController(gameId, playerInGame, playerActionMap),
              config.getLargePopUpWidth(),
              config.getLargePopUpHeight());
        });
      });

      // do a pop up right now
      Platform.runLater(() -> {
        App.loadPopUpWithController("my_development_cards.fxml",
            new PurchaseHandController(gameId, playerInGame, playerActionMap),
            config.getLargePopUpWidth(),
            config.getLargePopUpHeight());
      });
    }
  }

  //TODO: take out prints
  private void showBurnCardPopUp(GameInfo curGameInfo) {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    // generate special pop up for pairing card
    Map<String, Action> playerActionMap = curGameInfo.getPlayerActionMaps().get(viewerName);
    boolean allBurnActions = playerActionMap.values().stream()
        .allMatch(action -> action instanceof CardExtraAction
            && ((CardExtraAction) action).getCardEffect().equals(CardEffect.BURN_CARD));
    if (!playerActionMap.isEmpty() && allBurnActions) {
      // also assign the pending action button some functionality
      pendingActionButton.setDisable(false);
      pendingActionButton.setOnAction(event -> {
        Platform.runLater(() -> {
          App.loadPopUpWithController("free_card_pop_up.fxml",
              new BurnCardController(gameId, playerActionMap),
              config.getLargePopUpWidth(),
              config.getLargePopUpHeight());
        });
      });

      // do a pop up right now
      Platform.runLater(() -> {

        App.loadPopUpWithController("free_card_pop_up.fxml",
            new BurnCardController(gameId, playerActionMap),
            config.getLargePopUpWidth(),
            config.getLargePopUpHeight());
      });
    }
  }


  private void showFreeCardPopUp(GameInfo curGameInfo) {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    // generate special pop up for pairing card
    Map<String, Action> playerActionMap = curGameInfo.getPlayerActionMaps().get(viewerName);
    boolean allFreeActions = playerActionMap.values().stream()
        .allMatch(action -> action instanceof CardExtraAction
            && ((CardExtraAction) action).getCardEffect().equals(CardEffect.FREE_CARD));
    if (!playerActionMap.isEmpty() && allFreeActions) {
      // enable player to continue their pending action even they close the window
      pendingActionButton.setDisable(false);
      pendingActionButton.setOnAction(event -> {
        Platform.runLater(() -> {
          App.loadPopUpWithController("free_card_pop_up.fxml",
              new FreeCardPopUpController(gameId, playerActionMap),
              config.getSelectFreeCardWidth(),
              config.getSelectFreeCardHeight());
        });
      });

      // also, show a popup immediately
      Platform.runLater(() -> {
        App.loadPopUpWithController("free_card_pop_up.fxml",
            new FreeCardPopUpController(gameId, playerActionMap),
            config.getSelectFreeCardWidth(),
            config.getSelectFreeCardHeight());
      });
    }
  }


  private void showReserveNoblePopUp(GameInfo curGameInfo) {
    // generate special pop up for pairing card
    GameBoardLayoutConfig config = App.getGuiLayouts();
    Map<String, Action> playerActionMap = curGameInfo.getPlayerActionMaps().get(viewerName);
    boolean allReserveNobleActions = playerActionMap.values().stream()
        .allMatch(action -> action instanceof CardExtraAction
            && ((CardExtraAction) action).getCardEffect().equals(CardEffect.RESERVE_NOBLE));

    if (!playerActionMap.isEmpty() && allReserveNobleActions) {
      // enable player to continue their pending action even they close the window
      pendingActionButton.setDisable(false);
      pendingActionButton.setOnAction(event -> {
        Platform.runLater(() -> {
          App.loadPopUpWithController("noble_action_pop_up.fxml",
              new ActOnNoblePopUpController(gameId, playerActionMap, true),
              config.getSmallPopUpWidth() / 3 * 5,
              config.getSmallPopUpHeight());
        });
      });

      // also, show a popup immediately
      Platform.runLater(() -> {
        App.loadPopUpWithController("noble_action_pop_up.fxml",
            new ActOnNoblePopUpController(gameId, playerActionMap, true),
            config.getSmallPopUpWidth() / 3 * 5,
            config.getSmallPopUpHeight());
      });
    }
  }

  private void showBonusTokenPopUp(GameInfo curGameInfo) {
    // generate special pop up for pairing card
    Map<String, Action> playerActionMap = curGameInfo.getPlayerActionMaps().get(viewerName);
    boolean allBonusTokenActions = playerActionMap.values().stream()
        .allMatch(action -> action instanceof BonusTokenPowerAction);

    if (!playerActionMap.isEmpty() && allBonusTokenActions) {
      // enable player to continue their pending action even they close the window
      pendingActionButton.setDisable(false);
      pendingActionButton.setOnAction(event -> {
        Platform.runLater(() -> {
          App.loadPopUpWithController("bonus_token_pop_up.fxml",
              new BonusTokenPopUpController(gameId, playerActionMap),
              600,
              170);
        });
      });

      // also, show a popup immediately
      Platform.runLater(() -> {
        App.loadPopUpWithController("bonus_token_pop_up.fxml",
            new BonusTokenPopUpController(gameId, playerActionMap),
            600,
            170);
      });
    }
  }

  private void showClaimCityPopUp(GameInfo curGameInfo) {
    // generate special pop up for city card
    Map<String, Action> playerActionMap = curGameInfo.getPlayerActionMaps().get(viewerName);
    boolean allClaimCityActions = playerActionMap.values().stream()
        .allMatch(action -> action instanceof ClaimCityAction);

    if (!playerActionMap.isEmpty() && allClaimCityActions) {
      // enable player to continue their pending action even they close the window
      pendingActionButton.setDisable(false);
      pendingActionButton.setOnAction(event -> {
        Platform.runLater(() -> {
          App.loadPopUpWithController("claim_city_pop_up.fxml",
              new ClaimCityPopUpController(gameId, playerActionMap),
              300,
              400);
        });
      });

      // also, show a popup immediately
      Platform.runLater(() -> {
        App.loadPopUpWithController("claim_city_pop_up.fxml",
            new ClaimCityPopUpController(gameId, playerActionMap),
            300,
            400);
      });
    }

  }

  private void resetAllGameBoards(GameInfo curGameInfo) {
    // clear up all children in playerBoardAnchorPane
    for (BoardGui boardGui : extensionBoardGuiMap.values()) {
      Platform.runLater(boardGui::clearContent);
    }

    // First, check what extensions are we playing
    List<Extension> extensions = curGameInfo.getExtensions();
    TableTop tableTop = curGameInfo.getTableTop();
    Map<String, Action> playerActionMap;
    if (inWatchMode) {
      // if in watch mode, we always have empty action map
      playerActionMap = new HashMap<>();
    } else {
      playerActionMap = curGameInfo.getPlayerActionMaps().get(viewerName);
    }
    // generate BoardGui based on extension type
    for (Extension extension : extensions) {
      switch (extension) {
        case BASE:
          BaseBoardGui baseBoardGui = new BaseBoardGui(playerBoardAnchorPane, gameId);
          baseBoardGui.initialGuiActionSetup(tableTop, playerActionMap);
          extensionBoardGuiMap.put(extension, baseBoardGui);
          break;
        case ORIENT:
          OrientBoardGui orientBoardGui = new OrientBoardGui(playerBoardAnchorPane, gameId);
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
          CityBoardGui cityBoardGui = new CityBoardGui(playerBoardAnchorPane, gameId);
          cityBoardGui.initialGuiActionSetup(tableTop, playerActionMap);
          extensionBoardGuiMap.put(extension, cityBoardGui);
          break;
        default:
          break;
      }
    }
  }


  private void showFinishGamePopUp(GameInfo curGameInfo) {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    // the current game is finished (either done by Save OR GameOver)
    if (curGameInfo.isFinished()) {
      // should load a game over page (jump back to lobby after they click the button)
      // implicitly handle the threading stopping logic and loading back to lobby
      Platform.runLater(() -> {
        App.loadPopUpWithController("game_over.fxml",
            new GameOverPopUpController(mainGameUpdateThread,
                playerInfoThread,
                curGameInfo.getWinners(),
                false),
            config.getSmallPopUpWidth(),
            config.getSmallPopUpHeight());
      });
    }
  }

  private void resetPendingActionButton(GameInfo curGameInfo) {
    // always get the action map from game info
    Map<String, Action> playerActionMap = curGameInfo.getPlayerActionMaps()
        .get(viewerName);
    // player has finished one's pending action, set the button back to greyed out
    if (playerActionMap.isEmpty()) {
      pendingActionButton.setDisable(true);
    }
  }

  private void highlightPlayerInfoGui(GameInfo curGameInfo) {
    // highlight players accordingly
    String curTurnPlayerName = curGameInfo.getCurrentPlayer();
    if (!nameToPlayerInfoGuiMap.isEmpty()) {
      for (String name : nameToPlayerInfoGuiMap.keySet()) {
        nameToPlayerInfoGuiMap.get(name).setHighlight(name.equals(curTurnPlayerName));
      }
    }
  }


  private Thread generateGameInfoUpdateThread() {
    GameRequestSender gameRequestSender = App.getGameRequestSender();
    return new Thread(() -> {
      String hashedResponse = "";
      HttpResponse<String> longPullResponse = null;

      while (!Thread.currentThread().isInterrupted()) {
        // basic stuff needed for a long pull
        try {
          int responseCode = 408;
          while (responseCode == 408) {
            longPullResponse = gameRequestSender.sendGetGameInfoRequest(gameId, hashedResponse);
            responseCode = longPullResponse.getStatus();
            if (Thread.currentThread().isInterrupted()) {
              throw new InterruptedException(
                  "GameInfo Thread: " + Thread.currentThread().getName() +
                      " terminated");
            }
          }
          if (responseCode == 200) {
            // update the MD5 hash of previous response
            hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
            // decode this response into GameInfo class with Gson
            String responseInJsonString = longPullResponse.getBody();
            Gson gsonParser = SplendorDevHelper.getInstance().getGson();
            GameInfo curGameInfo = gsonParser.fromJson(responseInJsonString, GameInfo.class);
            // if the game is over, load the game over pop up page
            showFinishGamePopUp(curGameInfo);
            // these additional things can only happen if not in watch mode
            if (!inWatchMode) {
              // internally, check if the player has empty action map, if so
              // disable this pending action button
              resetPendingActionButton(curGameInfo);

              // TODO: <<<<< START OF OPTIONAL SECTION >>>>>>>>>
              // TODO: <<<<< This section contains the method that contains optional pop ups>>>>>>>>>
              // TODO: <<<<< conditions are check internally in method for readability      >>>>>>>>>

              // optionally, show the claim noble pop up, condition is checked inside method
              showClaimNoblePopUp(curGameInfo);
              // optionally, show the pairing card pop up, condition is checked inside method
              showPairingCardPopUp(curGameInfo);
              // optionally, show the taking a free card pop up, condition is checked inside method
              showFreeCardPopUp(curGameInfo);
              // optionally, show the taking a free card pop up, condition is checked inside method
              showBurnCardPopUp(curGameInfo);
              // optionally, show the taking a reserve noble pop up, condition is checked inside method
              showReserveNoblePopUp(curGameInfo);
              // optionally, show the take a token for free pop up, condition is checked inside method
              showBonusTokenPopUp(curGameInfo);
              // optionally, show the claiming a city pop up, condition is checked inside method
              showClaimCityPopUp(curGameInfo);
              // TODO: <<<<< END OF OPTIONAL SECTION >>>>>>>>>
            }

            // ALWAYS, reset all game boards gui based on the new game info
            resetAllGameBoards(curGameInfo);
            //ALWAYS, highlight the correct player gui based on the new game info
            highlightPlayerInfoGui(curGameInfo);

          }
        } catch (InterruptedException e) {
          System.out.println(Thread.currentThread().getName() + " is dead!");
          break;
        }
      }


    });
  }

  // interrupt the game update thread to save resources
  private EventHandler<ActionEvent> createClickOnSaveButtonEvent(GameInfo gameInfo, long gameId) {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    return event -> {
      App.loadPopUpWithController("save_game.fxml",
          new SaveGamePopUpController(gameInfo, gameId, playerInfoThread, mainGameUpdateThread),
          config.getSmallPopUpWidth(),
          config.getSmallPopUpHeight());
    };
  }

  // interrupt the game update thread to save resources
  private EventHandler<ActionEvent> createClickOnQuitButtonEvent() {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    return event -> {
      App.loadPopUpWithController("quit_game.fxml",
          new GameOverPopUpController(mainGameUpdateThread,
              playerInfoThread,
              new ArrayList<>(),
              true),
          config.getSmallPopUpWidth(), config.getSmallPopUpHeight());
    };
  }

  private void setupSaveGameButton(GameInfo curGameInfo) {
    // for all mode, these two should be like this
    saveButton.setDisable(true);
    quitButton.setOnAction(createClickOnQuitButtonEvent());
    // if not in watch mode, then we re-enable the button
    // otherwise, player can always save the game if they are in the game
    if (!inWatchMode) {
      saveButton.setDisable(false);
      saveButton.setOnAction(createClickOnSaveButtonEvent(curGameInfo, gameId));
    }
  }

  private void sortAllPlayerNames(GameInfo curGameInfo) {
    List<String> playerNames = curGameInfo.getPlayerNames();
    List<String> tmpPlayerNames = new ArrayList<>(playerNames);
    // sort the player names and store it to this game controller
    if (sortedPlayerNames.isEmpty()) {
      while (!tmpPlayerNames.get(0).equals(viewerName)) {
        String popPlayerName = tmpPlayerNames.remove(0);
        tmpPlayerNames.add(popPlayerName);
      }
      sortedPlayerNames = new ArrayList<>(tmpPlayerNames);
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // initially, there is no pending action at all!
    pendingActionButton.setDisable(true);
    // ban the purchase cards and reserve cards button from the watcher
    if (inWatchMode) {
      myCardButton.setDisable(true);
      myReservedCardsButton.setDisable(true);
    }

    GameRequestSender gameRequestSender = App.getGameRequestSender();
    HttpResponse<String> firstGameInfoResponse =
        gameRequestSender.sendGetGameInfoRequest(gameId, "");
    Gson gsonParser = SplendorDevHelper.getInstance().getGson();
    GameInfo curGameInfo = gsonParser.fromJson(firstGameInfoResponse.getBody(), GameInfo.class);
    // information about viewer's perspective
    if (viewerName == null) {
      viewerName = curGameInfo.getFirstPlayerName();
      // from now on, we can safely trust this viewerName as someone in the game
    }

    // enable the save game button for everyone (but the watchers) in the game
    setupSaveGameButton(curGameInfo);

    // sort player names based on different client views
    sortAllPlayerNames(curGameInfo);

    // if we are playing the Trading Extension, initialize the map of player name
    // to their arm code index
    List<Extension> extensionsPlaying = curGameInfo.getExtensions();
    List<String> playerNames = curGameInfo.getPlayerNames();
    if (extensionsPlaying.contains(Extension.TRADING_POST)) {
      for (int i = 1; i <= playerNames.size(); i++) {
        nameToArmCodeMap.put(playerNames.get(i - 1), i);
      }
    }

    // start thread to update player info
    playerInfoThread = generateAllPlayerInfoUpdateThread();
    // creating the thread as daemon to terminate them correctly
    playerInfoThread.setDaemon(true);
    playerInfoThread.start();

    // start thread to update game info
    mainGameUpdateThread = generateGameInfoUpdateThread();
    mainGameUpdateThread.setDaemon(true);
    mainGameUpdateThread.start();

  }
}
