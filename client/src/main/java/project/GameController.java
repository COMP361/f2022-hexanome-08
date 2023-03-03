package project;


import ca.mcgill.comp361.splendormodel.model.Colour;
import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.commons.codec.digest.DigestUtils;
import project.connection.SplendorServiceRequestSender;
import project.view.lobby.communication.User;
import project.view.splendor.BaseCardLevelGui;
import project.view.splendor.HorizontalPlayerInfoGui;
import project.view.splendor.NobleBoardGui;
import project.view.splendor.PlayerInfoGui;
import project.view.splendor.PlayerPosition;
import project.view.splendor.TokenBankGui;
import project.view.splendor.VerticalPlayerInfoGui;
import ca.mcgill.comp361.splendormodel.model.*;
import ca.mcgill.comp361.splendormodel.actions.*;

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

  private NobleBoardGui nobleBoard;

  private TokenBankGui tokenBankGui;

  private String prePlayerName;
  private final Map<Integer, BaseCardLevelGui> baseCardGuiMap = new HashMap<>();

  private final Map<String, PlayerInfoGui> nameToPlayerInfoGuiMap = new HashMap<>();

  private List<String> sortedPlayerNames = new ArrayList<>();

  public GameController(long gameId) {
    this.gameId = gameId;
  }


  /**
   * Opening the development cards pop up once "My Cards" button is pressed.
   */

  @FXML
  protected void onExitGameClick() throws IOException {
    App.setRoot("admin_lobby_page");
  }

  private Map<Colour, List<DevelopmentCard>> reorganizeCardsInHand(
      List<DevelopmentCard> allDevCards) {
    Map<Colour, List<DevelopmentCard>> result = new HashMap<>();
    for (DevelopmentCard card : allDevCards) {
      if (!result.containsKey(card.getGemColour())) {
        // initialize the list for cards
        List<DevelopmentCard> cardsOfOneColour = new ArrayList<>();
        cardsOfOneColour.add(card);
        result.put(card.getGemColour(), cardsOfOneColour);
      } else {
        // if result contains this colour before, then we just
        // need to add this card to the list the colour maps to
        //TODO: We need to sort them LATER!!!!
        result.get(card.getGemColour()).add(card);
      }

    }
    return result;
  }

  private EventHandler<ActionEvent> createOpenMyReserveCardClick(
      List<DevelopmentCard> reserveCards, List<NobleCard> reservedNobles) {
    return event -> {
      // TODO: Implement making reserveCards and reserveNobles into ImageViews
      Image img2 = new Image("project/pictures/noble/noble1.png");
      List<ImageView> testNobleImageViews = new ArrayList<>();
      for (int i = 0; i < 5; i++) {
        ImageView imgV = new ImageView(img2);
        testNobleImageViews.add(imgV);
      }

      try {
        App.loadPopUpWithController("my_reserved_cards.fxml",
            new ReservedHandController(testNobleImageViews, testNobleImageViews),
            800,
            600);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    };
  }

  private EventHandler<ActionEvent> createOpenMyPurchaseCardClick(
      List<DevelopmentCard> allDevCards) {
    return event -> {
      // TODO: Add a parameter for this method: Map<Colour, List<DevelopmentCard>> cardsMap here
      Map<Colour, List<DevelopmentCard>> colourToCardStackMap = reorganizeCardsInHand(allDevCards);
      try {
        App.loadPopUpWithController(
            "my_development_cards.fxml",
            new PurchaseHandController(colourToCardStackMap),
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


  //private Thread generateAllPlayerInfoUpdateThread(
  //    SplendorServiceRequestSender gameRequestSender, GameInfo firstGameInfo) {
  //  return new Thread(() -> {
  //    String hashedResponse = "";
  //    HttpResponse<String> longPullResponse = null;
  //    boolean isFirstCheck = true;
  //    while (true) {
  //      int responseCode = 408;
  //      while (responseCode == 408) {
  //        try {
  //          longPullResponse =
  //              gameRequestSender.sendGetAllPlayerInfoRequest(gameId, hashedResponse);
  //        } catch (UnirestException e) {
  //          throw new RuntimeException(e);
  //        }
  //        responseCode = longPullResponse.getStatus();
  //      }
  //
  //      if (responseCode == 200) {
  //        hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
  //        // decode this response into PlayerInGame class with Gson
  //        String responseInJsonString = longPullResponse.getBody();
  //        PlayerStates playerStates = new Gson().fromJson(responseInJsonString, PlayerStates.class);
  //        if (isFirstCheck) {
  //          int initTokenAmount =
  //              playerStates.getPlayersInfo().get(App.getUser().getUsername()).getTokenHand()
  //                  .getInitialAmount();
  //          try {
  //            setupPlayerInfoGui(playerStates,
  //                initTokenAmount,
  //                App.getGuiLayouts(),
  //                App.getUser(),
  //                firstGameInfo.getFirstPlayer());
  //          } catch (UnirestException e) {
  //            throw new RuntimeException(e);
  //          }
  //
  //          isFirstCheck = false;
  //        } else {
  //          for (PlayerInGame playerInfo : playerStates.getPlayersInfo().values()) {
  //            if (playerInfo.getName().equals(App.getUser().getUsername())) {
  //              List<DevelopmentCard> allDevCards =
  //                  playerInfo.getPurchasedHand().getDevelopmentCards();
  //              List<DevelopmentCard> allReserveCards =
  //                  playerInfo.getReservedHand().getDevelopmentCards();
  //              myCardButton
  //                  .setOnAction(createOpenMyPurchaseCardClick(allDevCards));
  //
  //              myReservedCardsButton
  //                  .setOnAction(
  //                      createOpenMyReserveCardClick(new ArrayList<>(), new ArrayList<>()));
  //            }
  //            updatePlayerInfoGui(playerInfo);
  //          }
  //        }
  //      }
  //    }
  //  });
  //}


  private void setupPlayerInfoGui(PlayerStates playerStates, int initTokenAmount,
                                  GameBoardLayoutConfig config, User curUser, String firstPlayer)
      throws UnirestException {

    // first check, then sort the player names accordingly based on different clients
    List<String> playerNames = new ArrayList<>(playerStates.getPlayersInfo().keySet());
    if (sortedPlayerNames.isEmpty()) {
      sortedPlayerNames = sortPlayerNames(App.getUser().getUsername(), playerNames);
    }
    HorizontalPlayerInfoGui btmPlayerGui =
        new HorizontalPlayerInfoGui(PlayerPosition.BOTTOM, curUser.getUsername(), initTokenAmount);
    nameToPlayerInfoGuiMap.put(curUser.getUsername(), btmPlayerGui);
    btmPlayerGui.setup(config.getBtmPlayerLayoutX(), config.getBtmPlayerLayoutY());
    List<HorizontalPlayerInfoGui> horizontalPlayers = new ArrayList<>();
    horizontalPlayers.add(btmPlayerGui);

    // The My PurchaseHand and My Reserve Hand buttons functionality assign
    // Do not do long pulling here (hash = "" -> instant response)
    //HttpResponse<String> inventoryResponse =
    //    gameRequestSender.sendGetPlayerInventoryRequest(
    //        gameId, curUser.getUsername(), curUser.getAccessToken(), "");
    PlayerInGame curPlayerInfo = playerStates.getPlayersInfo().get(curUser.getUsername());
    List<DevelopmentCard> allDevCards =
        curPlayerInfo.getPurchasedHand().getDevelopmentCards();
    List<DevelopmentCard> allReserveCards =
        curPlayerInfo.getReservedHand().getDevelopmentCards();
    myCardButton
        .setOnAction(createOpenMyPurchaseCardClick(allDevCards));

    myReservedCardsButton
        .setOnAction(createOpenMyReserveCardClick(new ArrayList<>(), new ArrayList<>()));

    // set up other player area with data from server
    String leftPlayerName = sortedPlayerNames.get(1);
    List<VerticalPlayerInfoGui> verticalPlayers = new ArrayList<>();
    VerticalPlayerInfoGui leftPlayerGui =
        new VerticalPlayerInfoGui(PlayerPosition.LEFT, leftPlayerName, initTokenAmount);
    // set up
    leftPlayerGui.setup(config.getLeftPlayerLayoutX(), config.getLeftPlayerLayoutY());
    // add to array, so that we can add them to gui at the same time later
    verticalPlayers.add(leftPlayerGui);

    // put them into global map for !firstCheck case
    nameToPlayerInfoGuiMap.put(leftPlayerName, leftPlayerGui);
    int curPlayersCount = sortedPlayerNames.size();
    if (curPlayersCount >= 3) {
      String topPlayerName = sortedPlayerNames.get(2);
      HorizontalPlayerInfoGui topPlayerGui =
          new HorizontalPlayerInfoGui(PlayerPosition.TOP, topPlayerName, initTokenAmount);
      topPlayerGui.setup(config.getTopPlayerLayoutX(), config.getTopPlayerLayoutY());

      horizontalPlayers.add(topPlayerGui);

      nameToPlayerInfoGuiMap.put(topPlayerName, topPlayerGui);
      if (curPlayersCount == 4) {
        String rightPlayerName = sortedPlayerNames.get(3);
        VerticalPlayerInfoGui rightPlayerGui =
            new VerticalPlayerInfoGui(PlayerPosition.RIGHT, rightPlayerName, initTokenAmount);

        rightPlayerGui.setup(config.getRightPlayerLayoutX(),
            config.getRightPlayerLayoutY());
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


  private static Gson getActionGson() {
    final RuntimeTypeAdapterFactory<Action> actionFactory = RuntimeTypeAdapterFactory
        .of(Action.class, "type")
        .registerSubtype(ReserveAction.class)
        .registerSubtype(PurchaseAction.class)
        .registerSubtype(TakeTokenAction.class);

    return new GsonBuilder().registerTypeAdapterFactory(actionFactory).create();

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
    SplendorServiceRequestSender gameRequestSender = App.getGameRequestSender();

    System.out.println("Current user: " + App.getUser().getUsername());
    System.out.println(gameRequestSender.getGameServiceName());
    try {
      HttpResponse<String> firstGameInfoResponse =
          gameRequestSender.sendGetGameInfoRequest(gameId, "");
      Gson gsonParser = SplendorDevHelper.getInstance().getGson();
      GameInfo curGameInfo = gsonParser.fromJson(firstGameInfoResponse.getBody(), GameInfo.class);
      //Thread playerInfoRelatedThread =
      //    generateAllPlayerInfoUpdateThread(gameRequestSender, curGameInfo);
      //Thread mainGameUpdateThread = generateGameInfoUpdateThread(gameRequestSender);
      //// start the thread for main board and playerInfo update at the same time
      //playerInfoRelatedThread.start();
      //mainGameUpdateThread.start();
    } catch (UnirestException e) {
      throw new RuntimeException(e);
    }

  }
}
