package project;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import project.connection.SplendorServiceRequestSender;
import project.view.lobby.User;
import project.view.splendor.BaseCardLevelGui;
import project.view.splendor.CardType;
import project.view.splendor.Colour;
import project.view.splendor.DevelopmentCardBoardGui;
import project.view.splendor.HorizontalPlayerInfoGui;
import project.view.splendor.NobleBoardGui;
import project.view.splendor.PlayerInfoGui;
import project.view.splendor.PlayerPosition;
import project.view.splendor.TokenBankGui;
import project.view.splendor.VerticalPlayerInfoGui;
import project.view.splendor.communication.Action;
import project.view.splendor.communication.Card;
import project.view.splendor.communication.CardAction;
import project.view.splendor.communication.DevelopmentCard;
import project.view.splendor.communication.GameInfo;
import project.view.splendor.communication.PlayerInGame;
import project.view.splendor.communication.Position;
import project.view.splendor.communication.PurchaseAction;
import project.view.splendor.communication.TakeTokenAction;

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

  public GameController(long gameId) {
    this.gameId = gameId;
  }


  /**
   * Opening the development cards pop up once "My Cards" button is pressed.
   */

  private final Map<Integer, Map<CardType, DevelopmentCardBoardGui>>
      levelCardsMap = new HashMap<>();

  private final Map<String, PlayerInfoGui> nameToPlayerInfoGuiMap = new HashMap<>();

  private List<String> sortedPlayerNames = new ArrayList<>();

  @FXML
  protected void onExitGameClick() throws IOException {
    App.setRoot("admin_lobby_page");
  }

  @FXML
  protected void onOpenMyReserveCardClick() throws IOException {

    Image img2 = new Image("project/pictures/noble/noble1.png");
    List<ImageView> testNobleImageViews = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      ImageView imgV = new ImageView(img2);
      testNobleImageViews.add(imgV);
    }
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("my_reserved_cards.fxml"));
    fxmlLoader.setController(new ReservedHandController(testNobleImageViews, testNobleImageViews));
    Stage newStage = new Stage();
    newStage.setTitle("My Reserved Cards");
    newStage.setScene(new Scene(fxmlLoader.load(), 789, 406));
    newStage.getIcons().add(new Image("project/pictures/back/splendor-icon.jpg"));
    newStage.show();
  }


  private Map<Colour, List<DevelopmentCard>> reorganizeCardsInHand
      (List<DevelopmentCard> allDevCards) {
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
      List<DevelopmentCard> reserveCards) {
    return event -> {
      // TODO: Implement this later
    };
  }

  private EventHandler<ActionEvent> createOpenMyPurchaseCardClick(
      List<DevelopmentCard> allDevCards) {
    return event -> {
      // TODO: Add a parameter for this method: Map<Colour, List<DevelopmentCard>> cardsMap here

      FXMLLoader fxmlLoader =
          new FXMLLoader(App.class.getResource("my_development_cards.fxml"));

      Map<Colour, List<DevelopmentCard>> colourToCardStackMap = reorganizeCardsInHand(allDevCards);
      fxmlLoader.setController(new PurchaseHandController(colourToCardStackMap));
      Stage newStage = new Stage();
      newStage.setTitle("My Purchased Cards");
      newStage.getIcons().add(new Image("project/pictures/back/splendor-icon.jpg"));
      try {
        newStage.setScene(new Scene(fxmlLoader.load(), 800, 600));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      newStage.show();
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


  private void runGameInfoUpdateThread() {
    // implicitly contains the GUI update to tableTop as well
    Thread mainThread = new Thread(() -> {


    });

    mainThread.start();

  }

  // This will update the current player's Inventory in time
  private Thread generateCurrentPlayerUpdateThread(
      SplendorServiceRequestSender gameRequestSender,
      long gameId, String playerName, String accessToken,
      Map<String, PlayerInfoGui> stringPlayerInfoGuiMap) {
    // player specific updates happen here
    return new Thread(() -> {
      String hashedResponse = "";
      ResponseEntity<String> longPullResponse = null;
      // For now, even it's now the first check, we generate ALL
      // the GUI on the fly, we don't care about speed for now
      while (true) {
        int responseCode = 408;
        while (responseCode == 408) {
          longPullResponse =
              gameRequestSender.sendGetPlayerInventoryRequest(
                  gameId, playerName, accessToken, hashedResponse);
          responseCode = longPullResponse.getStatusCode().value();
        }
        // no point of updating if the GUI is not there yet OR nothing changed
        if (responseCode == 200) {
          // && stringPlayerInfoGuiMap.containsKey(playerName)
          // update the MD5 hash of previous response
          hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
          // decode this response into PlayerInGame class with Gson
          String responseInJsonString = longPullResponse.getBody();
          PlayerInGame curPlayerInventory =
              new Gson().fromJson(responseInJsonString, PlayerInGame.class);
          int newPoints = curPlayerInventory.getPrestigePoints();
          EnumMap<Colour, Integer> newTokenInHand =
              curPlayerInventory.getTokenHand().getAllTokens();
          List<DevelopmentCard> allDevCards = curPlayerInventory.getPurchasedHand()
              .getDevelopmentCards();
          // Get the player gui
          PlayerInfoGui playerInfoGui = stringPlayerInfoGuiMap.get(playerName);
          // updating the GUI based on the new info from server
          Platform.runLater(() -> {
            // update the public-player associated area
            playerInfoGui.setNewPrestigePoints(newPoints);
            playerInfoGui.setNewTokenInHand(newTokenInHand);
            playerInfoGui.setGemsInHand(allDevCards);
          });
        }
      }

    });

  }


  private void setUpPlayerGui(GameInfo curGameInfo,
                              GameBoardLayoutConfig config,
                              User curUser,
                              SplendorServiceRequestSender gameRequestSender) {
    // sort the names, and this shall never happen again
    if (sortedPlayerNames.isEmpty()) {
      sortedPlayerNames = sortPlayerNames(curUser.getUsername(), curGameInfo.getPlayerNames());

    }

    int initTokenToStart = curGameInfo.getTableTop().getBank().getInitialValue();
    HorizontalPlayerInfoGui btmPlayerGui =
        new HorizontalPlayerInfoGui(PlayerPosition.BOTTOM, curUser.getUsername(), initTokenToStart);
    nameToPlayerInfoGuiMap.put(curUser.getUsername(), btmPlayerGui);
    btmPlayerGui.setup(config.getBtmPlayerLayoutX(), config.getBtmPlayerLayoutY());
    List<HorizontalPlayerInfoGui> horizontalPlayers = new ArrayList<>();
    horizontalPlayers.add(btmPlayerGui);

    // The My PurchaseHand and My Reserve Hand buttons functionality assign
    // Do not do long pulling here (hash = "" -> instant response)
    ResponseEntity<String> inventoryResponse =
        gameRequestSender.sendGetPlayerInventoryRequest(
            gameId, curUser.getUsername(), curUser.getAccessToken(), "");
    PlayerInGame curPlayerInfo =
        new Gson().fromJson(inventoryResponse.getBody(), PlayerInGame.class);
    List<DevelopmentCard> allDevCards =
        curPlayerInfo.getPurchasedHand().getDevelopmentCards();
    List<DevelopmentCard> allReserveCards =
        curPlayerInfo.getReservedHand().getDevelopmentCards();

    myCardButton
        .setOnAction(createOpenMyPurchaseCardClick(allDevCards));

    myReservedCardsButton
        .setOnAction(createOpenMyReserveCardClick(allReserveCards));


    // set up other player area with data from server
    String leftPlayerName = sortedPlayerNames.get(1);
    List<VerticalPlayerInfoGui> verticalPlayers = new ArrayList<>();
    int curPlayersCount = sortedPlayerNames.size();

    VerticalPlayerInfoGui leftPlayerGui =
        new VerticalPlayerInfoGui(PlayerPosition.LEFT, leftPlayerName, initTokenToStart);
    // set up
    leftPlayerGui.setup(config.getLeftPlayerLayoutX(), config.getLeftPlayerLayoutY());
    // add to array, so that we can add them to gui at the same time later
    verticalPlayers.add(leftPlayerGui);

    // put them into global map for !firstCheck case
    nameToPlayerInfoGuiMap.put(leftPlayerName, leftPlayerGui);

    if (curPlayersCount >= 3) {
      String topPlayerName = sortedPlayerNames.get(2);
      HorizontalPlayerInfoGui topPlayerGui =
          new HorizontalPlayerInfoGui(PlayerPosition.TOP, topPlayerName,
              initTokenToStart);
      topPlayerGui.setup(config.getTopPlayerLayoutX(), config.getTopPlayerLayoutY());

      horizontalPlayers.add(topPlayerGui);

      nameToPlayerInfoGuiMap.put(topPlayerName, topPlayerGui);
      if (curPlayersCount == 4) {
        String rightPlayerName = sortedPlayerNames.get(3);
        VerticalPlayerInfoGui rightPlayerGui =
            new VerticalPlayerInfoGui(PlayerPosition.RIGHT, rightPlayerName,
                initTokenToStart);

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

    String firstPlayer = curGameInfo.getFirstPlayer();
    // at this point, the map can not be empty, safely get the playerGui
    PlayerInfoGui firstPlayerGui = nameToPlayerInfoGuiMap.get(firstPlayer);
    firstPlayerGui.setHighlight(true);

  }


  public static Gson getActionGson() {
    RuntimeTypeAdapterFactory<Action> actionFactory =
        RuntimeTypeAdapterFactory
            .of(Action.class, "type")
            .registerSubtype(CardAction.class, CardAction.class.getName())
            .registerSubtype(TakeTokenAction.class, TakeTokenAction.class.getName());

    return new GsonBuilder()
        .registerTypeAdapterFactory(actionFactory).create();

  }

  @Override
  // TODO: This method contains what's gonna happen after clicking "play" on the board
  public void initialize(URL url, ResourceBundle resourceBundle) {
    GameBoardLayoutConfig config = App.getGuiLayouts();
    SplendorServiceRequestSender gameRequestSender = App.getGameRequestSender();
    User curUser = App.getUser(); // at this point, user will not be Null

    Thread mainGameUpdateThread = new Thread(() -> {
      // basic stuff needed for a long pull
      String hashedResponse = "";
      ResponseEntity<String> longPullResponse = null;
      boolean isFirstCheck = true;

      while (true) {
        // always do one, just in case
        try {
          App.refreshUserToken(curUser);
          // after this, curUser.getAccessToken() will for sure have a valid token
        } catch (UnirestException e) {
          throw new RuntimeException(e);
        }

        int responseCode = 408;
        while (responseCode == 408) {
          longPullResponse = gameRequestSender.sendGetGameInfoRequest(gameId, hashedResponse);
          responseCode = longPullResponse.getStatusCode().value();
        }

        if (responseCode == 200) {
          try {
            // with calling this, we can make sure any requests involve
            // using access token will not fail due to expiration
            App.refreshUserToken(curUser);
          } catch (UnirestException e) {
            throw new RuntimeException(e);
          }

          // update the MD5 hash of previous response
          hashedResponse = DigestUtils.md5Hex(longPullResponse.getBody());
          // decode this response into PlayerInGame class with Gson
          String responseInJsonString = longPullResponse.getBody();
          GameInfo curGameInfo = new Gson().fromJson(responseInJsonString, GameInfo.class);
          int curPlayerNum = curGameInfo.getPlayerNames().size();
          // how we are going to parse from this curGameInfo depends on first check or not
          if (isFirstCheck) {
            //Platform.runLater(() -> {
            //  playerBoardAnchorPane.getChildren().clear();
            //});
            // set up all player related GUI
            setUpPlayerGui(curGameInfo, config, curUser, gameRequestSender);

            // TODO: Potential nested clickable noble cards
            // initialize noble area
            NobleBoardGui nobleBoard = new NobleBoardGui(100, 100, 5);
            Platform.runLater(() -> {
              nobleBoard.setup(curPlayerNum, config.getNobleLayoutX(), config.getNobleLayoutY());
              playerBoardAnchorPane.getChildren().add(nobleBoard);
            });

            // TODO: Assign the Action hashed String to the confirm button of token bank later!!!
            // initialize token area
            TokenBankGui tokenBank = new TokenBankGui();
            Platform.runLater(() -> {
              tokenBank.setup(curPlayerNum, config.getTokenBankLayoutX(),
                  config.getTokenBankLayoutY());
              playerBoardAnchorPane.getChildren().add(tokenBank);
            });

            // initialize the base board
            ResponseEntity<String> actionMapResponse =
                gameRequestSender.sendGetPlayerActionsRequest(
                    gameId, curUser.getUsername(), curUser.getAccessToken());
            Type empMapType = new TypeToken<Map<String, Action>>() {
            }.getType();
            Gson actionGson = GameController.getActionGson();
            Map<String, Action> resultActionsMap =
                actionGson.fromJson(actionMapResponse.getBody(), empMapType);

            // if the action map is not empty
            if (!resultActionsMap.isEmpty()) {
              for (int i = 3; i >= 1; i--) {
                Map<CardType, DevelopmentCardBoardGui> oneLevelCardsMap = new HashMap<>();
                Card[] cardsInCurLevel =
                    curGameInfo.getTableTop().getBaseBoard().getCardBoard()[3 - i];
                List<DevelopmentCard> cards = new ArrayList<>();
                for (Card c : cardsInCurLevel) {
                  cards.add((DevelopmentCard) c);
                }
                BaseCardLevelGui baseCardLevelGui = new BaseCardLevelGui(i, cards);
                baseCardLevelGui.setup();
                oneLevelCardsMap.put(CardType.BASE, baseCardLevelGui);

                Platform.runLater(() -> {
                  baseCardBoard.getChildren().add(baseCardLevelGui);
                });
                levelCardsMap.put(i, oneLevelCardsMap);
              }

              String[][][] actionHashesLookUp = new String[3][4][2];
              for (String actionHash : resultActionsMap.keySet()) {
                Action curAction = resultActionsMap.get(actionHash);
                if (curAction.getIsCardAction()) {
                  CardAction cardAction = (CardAction) curAction;
                  Position cardPosition = cardAction.getPosition();
                  DevelopmentCard curCard = (DevelopmentCard) cardAction.getCard();
                  int level = curCard.getLevel();
                  int xIndex = cardPosition.getX();
                  if (curAction instanceof PurchaseAction) {
                    actionHashesLookUp[level][xIndex][0] = actionHash;
                  } else {
                    actionHashesLookUp[level][xIndex][1] = actionHash;
                  }
                } else {
                  // TODO: LATER, TAKE TOKEN ACTION OR SOMETHING ELSE
                }
              }

              // Since we now have all String[2] actionHashes, we can go and
              // assign them to cards
              for (int i = 0; i < 3; i++) {
                levelCardsMap
                    .get(3 - i)
                    .get(CardType.BASE)
                    .bindActionToCardAndDeck(actionHashesLookUp[i], gameId);
              }

            }

            //isFirstCheck = false;

            // start long pulling on client-user related GUI updates
            // The thread that monitors changes related to this player specifically
            // just need to do this thread once for every client first check
            Thread playerSpecificThread = generateCurrentPlayerUpdateThread(gameRequestSender,
                gameId, curUser.getUsername(), curUser.getAccessToken(), nameToPlayerInfoGuiMap);
            //playerSpecificThread.start();

          }

          // If it is not first check.....
          else {


          }
        }
      }
    });

    mainGameUpdateThread.start();


    // TODO: HARDCODED GUI REPRESENTATION OF ORIENT AND BASE
    // base card and orient card area
    //for (int i = 3; i >= 1; i--) {
    //  Map<CardType, DevelopmentCardBoardGui> oneLevelCardsMap = new HashMap<>();
    //  BaseCardLevelGui baseCardLevelGui = new BaseCardLevelGui(i, cards);
    //  OrientCardLevelGui orientCardLevelGui = new OrientCardLevelGui(i, cards2);
    //
    //  baseCardLevelGui.setup();
    //  orientCardLevelGui.setup();
    //  oneLevelCardsMap.put(CardType.BASE, baseCardLevelGui);
    //  oneLevelCardsMap.put(CardType.ORIENT, orientCardLevelGui);
    //
    //  Platform.runLater(() -> {
    //    baseCardBoard.getChildren().add(baseCardLevelGui);
    //    orientCardBoard.getChildren().add(orientCardLevelGui);
    //  });
    //  levelCardsMap.put(i, oneLevelCardsMap);
    //}
  }
}
