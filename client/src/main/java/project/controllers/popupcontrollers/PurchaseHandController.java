package project.controllers.popupcontrollers;


import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.CardExtraAction;
import ca.mcgill.comp361.splendormodel.model.CardEffect;
import ca.mcgill.comp361.splendormodel.model.Colour;
import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
import ca.mcgill.comp361.splendormodel.model.NobleCard;
import ca.mcgill.comp361.splendormodel.model.Position;
import ca.mcgill.comp361.splendormodel.model.PurchasedHand;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import project.App;
import project.connection.GameRequestSender;
import project.view.splendor.ActionIdPair;

/**
 * Purchase controller class, controls the visual updates happening in purchase hand.
 * associated with "my_development_cards.fxml"
 */
public class PurchaseHandController implements Initializable {

  private final Map<Colour, Group> colourGroupMap;
  private final Map<Colour, List<DevelopmentCard>> colourCardsMap;
  private final List<NobleCard> nobleCards;
  private final Map<String, Action> playerActions;
  private final Rectangle coverRectangle;
  private final long gameId;
  @FXML
  // each index represent the group of displaying all cards of one colour
  // there are 6 colours to display (hold the Groups)
  private HBox cardStackHbox;
  @FXML
  // place to store the gold token cards.
  // hold the Gold->List<DevCards>
  private VBox goldTokenCardsVbox;
  @FXML
  // hold List<NobleCard>
  private VBox noblesUnLocked;

  /**
   * PurchaseHandController.
   *
   * @param purchasedHand  purchasedHand
   * @param playerActions  playerActions
   * @param coverRectangle coverRectangle
   */
  public PurchaseHandController(long gameId, PurchasedHand purchasedHand,
                                Map<String, Action> playerActions,
                                Rectangle coverRectangle) {
    // organize all dev cards (including gold colour ones) into colour map
    this.gameId = gameId;
    List<DevelopmentCard> allCardsInHand = purchasedHand.getDevelopmentCards();
    this.colourCardsMap = reorganizeCardsInHand(allCardsInHand);
    this.colourGroupMap = new HashMap<>();
    this.nobleCards = purchasedHand.getNobleCards();
    this.playerActions = playerActions;
    this.coverRectangle = coverRectangle;
  }


  /**
   * Put cards in different groups based on colour type.
   * The colour can be gold.
   *
   * @param allDevCards allDevCards
   * @return return a map
   */
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

  // Add the satchel mark display to every dev card (exclude the orient gold card)
  private List<HBox> generateCardSatchelPair(
      List<DevelopmentCard> oneColourCards,
      Map<Position, List<ActionIdPair>> positionSatchelActionMap) {

    List<HBox> result = new ArrayList<>();
    for (DevelopmentCard card : oneColourCards) {
      Rectangle satchelMark = new Rectangle();
      // Colour will be assigned differently if the card is linked
      if (card.isPaired()) {
        satchelMark.setFill(Color.BLUE);
      } else {
        satchelMark.setFill(Color.WHITESMOKE);
      }
      Image img;
      // has some effect -> orient
      if (!card.isBaseCard()) {
        img = new Image(App.getOrientCardPath(card.getCardName(), card.getLevel()));
      } else { // otherwise, base
        img = new Image(App.getBaseCardPath(card.getCardName(), card.getLevel()));
      }
      ImageView imgV = new ImageView(img);
      imgV.setFitWidth(100);
      imgV.setFitHeight(150);
      satchelMark.setWidth(20);
      satchelMark.setHeight(150);

      // if we have some pair actions, assign them
      if (!positionSatchelActionMap.isEmpty()) {
        List<List<ActionIdPair>> satchelActions = positionSatchelActionMap.values().stream()
            .filter(e -> ((CardExtraAction) e.get(0).getAction()).getCurCard().equals(card))
            .collect(Collectors.toList());
        if (satchelActions.size() > 0) {
          imgV.setOnMouseClicked(createClickOnCardToPair(gameId, satchelActions.get(0)));
        }
      }
      HBox container = new HBox(imgV, satchelMark);
      result.add(container);
    }
    return result;
  }

  // display it a bit inclined
  private void addCardSatchelPairToColourGroup(
      List<HBox> cardSatchelPairs, Group groupOfOneColour) {

    for (HBox pair : cardSatchelPairs) {
      groupOfOneColour.getChildren().add(pair);
    }

    int i = 0;
    for (Node n : groupOfOneColour.getChildren()) {
      n.setLayoutX(i * 10);
      n.setLayoutY(i * 20);
      i += 1;
    }
  }

  private void addGoldTokenCards(List<DevelopmentCard> goldTokenCards, VBox goldTokenCardsVbox) {
    if (goldTokenCards != null && goldTokenCards.size() > 0) {
      for (DevelopmentCard card : goldTokenCards) {
        String cardPath = App.getOrientCardPath(card.getCardName(), card.getLevel());
        Image image = new Image(cardPath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(150);
        // add the image view to vbox
        goldTokenCardsVbox.getChildren().add(imageView);
      }
    }
  }

  private void addNobleCards(List<NobleCard> nobleCards, VBox noblesUnLocked) {
    for (NobleCard nobleCard : nobleCards) {
      String noblePath = App.getNoblePath(nobleCard.getCardName());
      ImageView imageView = new ImageView(new Image(noblePath));
      // add the image view to vbox
      imageView.setFitWidth(120);
      imageView.setFitHeight(120);
      noblesUnLocked.getChildren().add(imageView);
    }
  }

  //private List<HBox> generateCardSatchelPairWithActions(
  //    List<DevelopmentCard> oneColourCards, Map<Position,
  //    List<ActionIdPair>> positionToActionMap) {
  //  List<HBox> result = new ArrayList<>();
  //  for (DevelopmentCard card : oneColourCards) {
  //    Rectangle satchelMark = new Rectangle();
  //    // Colour will be assigned differently if the card is linked
  //    if (card.isPaired()) {
  //      satchelMark.setFill(Color.BLUE);
  //    } else {
  //      satchelMark.setFill(Color.WHITESMOKE);
  //    }
  //    Image img;
  //    // has some effect -> orient
  //    if (!card.isBaseCard()) {
  //      img = new Image(App.getOrientCardPath(card.getCardName(), card.getLevel()));
  //    } else { // otherwise, base
  //      img = new Image(App.getBaseCardPath(card.getCardName(), card.getLevel()));
  //    }
  //    ImageView imgV = new ImageView(img);
  //    imgV.setFitWidth(100);
  //    imgV.setFitHeight(150);
  //    satchelMark.setWidth(20);
  //    satchelMark.setHeight(150);
  //    HBox container = new HBox(imgV, satchelMark);
  //    result.add(container);
  //  }
  //  return result;
  //
  //
  //}

  private EventHandler<MouseEvent> createClickOnCardToPair(long gameId,
                                                           List<ActionIdPair> satchelAction) {
    return event -> {
      // it's clickable, we can send some requests here

      GameRequestSender gameRequestSender = App.getGameRequestSender();
      String playerName = App.getUser().getUsername();
      String accessToken = App.getUser().getAccessToken();
      String actionId = satchelAction.get(0).getActionId();
      // sends a POST request that tells the server which action we chose
      gameRequestSender.sendPlayerActionChoiceRequest(gameId, playerName, accessToken, actionId);
      ImageView imageView = (ImageView) event.getSource();
      Stage curWindow = (Stage) imageView.getScene().getWindow();
      curWindow.close();
    };
  }


  private Map<Position, List<ActionIdPair>> getSatchelActionsInPurchaseHand(
      Map<String, Action> satchelActions) {
    Map<Position, List<ActionIdPair>> positionToActionMap = new HashMap<>();
    // assign actions to positions (each position can have a list of action pair associated)
    for (String actionId : satchelActions.keySet()) {
      Action action = satchelActions.get(actionId);
      Position cardPosition;
      CardExtraAction satchelAction = (CardExtraAction) action;
      cardPosition = satchelAction.getCardPosition();
      List<ActionIdPair> actions = new ArrayList<>();
      actions.add(new ActionIdPair(actionId, action));
      positionToActionMap.put(cardPosition, actions);
    }

    return positionToActionMap;
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    //Map<String, Action> satchelActions = playerActions.entrySet()
    //    .stream().filter(e -> e.getValue() instanceof CardExtraAction &&
    //        ((CardExtraAction) e).getCardEffect().equals(CardEffect.SATCHEL))
    //    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    Map<String, Action> satchelActions = new HashMap<>();
    for (String actionId : playerActions.keySet()) {
      Action action = playerActions.get(actionId);
      if (action instanceof CardExtraAction) {
        CardExtraAction cardExtraAction = (CardExtraAction) action;
        if (cardExtraAction.getCardEffect().equals(CardEffect.SATCHEL)) {
          satchelActions.put(actionId, action);
        }
      }
    }


    Map<Position, List<ActionIdPair>> positionSatchelActionMap = new HashMap<>();
    if (satchelActions.size() > 0) {
      positionSatchelActionMap = getSatchelActionsInPurchaseHand(satchelActions);
    }

    Colour[] baseColours = App.getBaseColours();
    // the groups are stored in HBox in FXML file, there are 6 elements in the HBox
    // the first 5 are groups that store regular dev cards
    // the 6th element is the VBox storing gold orient cards
    for (int i = 0; i < 5; i++) {
      Colour curColour = baseColours[i];
      Group colourGroup = (Group) cardStackHbox.getChildren().get(i);
      // store a colour map look up reference to the GUI group so that it
      // is easier to have access to it rather than counting the index from 0 to 4
      colourGroupMap.put(curColour, colourGroup);
    }

    // add the gold colour cards' image views to vbox
    List<DevelopmentCard> goldTokenCards = colourCardsMap.get(Colour.GOLD);
    addGoldTokenCards(goldTokenCards, goldTokenCardsVbox);

    for (Colour c : colourGroupMap.keySet()) {
      if (colourCardsMap.containsKey(c)) {
        List<DevelopmentCard> cardsOfOneColour = colourCardsMap.get(c);
        //TODO: assign actions to image views with playerActions, depending on what kind of
        // actions (only CardExtraAction of Satchel can happen in this purchase hand,
        // only for normal
        // cards with RED, WHITE, BLUE, GREEN, BLACK colours)
        // Bind actions to image view during generateCardSatchelPair(...) method
        for (DevelopmentCard card : cardsOfOneColour) {
          System.out.println(card.getCardName());
        }

        List<HBox> allPairs = generateCardSatchelPair(cardsOfOneColour, positionSatchelActionMap);
        addCardSatchelPairToColourGroup(allPairs, colourGroupMap.get(c));
      }
    }

    // display the noble cards to GUI
    addNobleCards(nobleCards, noblesUnLocked);

  }
}
