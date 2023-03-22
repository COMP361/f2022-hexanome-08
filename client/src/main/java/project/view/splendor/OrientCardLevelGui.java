package project.view.splendor;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.PurchaseAction;
import ca.mcgill.comp361.splendormodel.actions.ReserveAction;
import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
import ca.mcgill.comp361.splendormodel.model.Position;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import project.App;
import project.controllers.popupcontrollers.CardActionController;
import project.controllers.popupcontrollers.DeckActionController;

/**
 * TODO: Creates the GUI for a list of orient level cards.
 */
public class OrientCardLevelGui extends HBox implements DevelopmentCardBoardGui {
  private final int level;
  private DevelopmentCard[] cards;
  private List<DevelopmentCard> deck;

  /**
   * Constructs the Orient Card GUI.
   *
   * @param level provides the level of the Orient Cards.
   * @param cards provides the list of cards you want displayed in the GUI.
   */
  public OrientCardLevelGui(int level, DevelopmentCard[] cards, List<DevelopmentCard> deck) {
    this.level = level;
    this.cards = cards;
    this.deck = deck;
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/project/orient_card_template.fxml"));
    fxmlLoader.setRoot(this);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public double getLevel() {
    return level;
  }

  @Override
  public List<ImageView> getAllCardsGui() {
    List<ImageView> allCards = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      ImageView curImageView = (ImageView) this.getChildren().get(i);
      allCards.add(curImageView);
    }
    return allCards;
  }

  @Override
  public ImageView getOneCardGui(int cardIndex) {
    return getAllCardsGui().get(cardIndex);
  }

  private void setUpCards(DevelopmentCard[] cards) {
    for (int i = 0; i < cards.length; i++) {
      DevelopmentCard card = cards[i];
      String curCardName = card.getCardName();
      int curCardLevel = card.getLevel();
      Image cardImg;
      if (card.getPrestigePoints() >= 0) {
        String cardPath = App.getOrientCardPath(curCardName, curCardLevel);
        cardImg = new Image(cardPath);
      } else {
        cardImg = null;
      }
      getOneCardGui(i).setImage(cardImg);
    }
  }

  private EventHandler<MouseEvent> createClickOnCardHandler(long gameId,
                                                            List<ActionIdPair> allActions) {
    return event -> {
      App.loadPopUpWithController("card_action.fxml",
          new CardActionController(gameId, allActions, null), 360, 170);
    };
  }

  private EventHandler<MouseEvent> createClickOnDeckHandler(long gameId, String actionId) {
    return event -> {
      App.loadPopUpWithController("deck_action.fxml",
          new DeckActionController(gameId, actionId),
          App.getGuiLayouts().getSmallPopUpWidth(),
          App.getGuiLayouts().getSmallPopUpHeight());
    };
  }

  private void setDeckLevelText() {
    Group levelCard = (Group) this.getChildren().get(2);
    Rectangle rectangle = (Rectangle) levelCard.getChildren().get(0);
    int newRemainingCards = deck.size();
    rectangle.setFill(Color.RED);
    Text deck = (Text) levelCard.getChildren().get(1);
    Text levelOfCard = (Text) levelCard.getChildren().get(2);
    deck.setText("10");
    deck.setFont(Font.font("System", FontPosture.REGULAR, 16));
    if (this.level == 3) {
      deck.setText(newRemainingCards + "");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 16));
      levelOfCard.setText(". . .");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 18));
    } else if (this.level == 2) {
      deck.setText(newRemainingCards + "");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 16));
      levelOfCard.setText(" . .");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 18));
    } else if (this.level == 1) {
      deck.setText(newRemainingCards + "");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 16));
      levelOfCard.setText(" .");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 18));
    }
  }

  public void setCards(DevelopmentCard[] cards) {
    this.cards = cards;
  }

  public void setDeck(List<DevelopmentCard> deck) {
    this.deck = deck;
  }


  @Override
  public void bindActionToCardAndDeck(Map<Position, List<ActionIdPair>> positionToActionMap,
                                      long gameId) {
    Map<Position, List<ActionIdPair>> curLevelMap = positionToActionMap.entrySet()
        .stream().filter(e -> e.getKey().getX() == level)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    List<ImageView> allCardsImageViews = getAllCardsGui();
    for (Position position : curLevelMap.keySet()) {
      if (position.getY() == -1) {
        // assign reserve action to deck (deck is only clickable if player has such action)
        Group deck = (Group) this.getChildren().get(2);
        ActionIdPair actionIdPair = curLevelMap.get(position).get(0);
        String actionId = actionIdPair.getActionId();
        deck.setOnMouseClicked(createClickOnDeckHandler(gameId, actionId));
      } else {
        List<ActionIdPair> allActions = curLevelMap.get(position);
        // get the card from the action of action id pair
        Action action = allActions.get(0).getAction();
        DevelopmentCard cardBindingActionTo;
        if (action instanceof PurchaseAction) {
          cardBindingActionTo = ((PurchaseAction) action).getCurCard();
        } else {
          cardBindingActionTo = ((ReserveAction) action).getCurCard();
        }
        // only bind the image view some actions if it's not dummy
        if (cardBindingActionTo.getPrestigePoints() >= 0) {
          // it's not a dummy card
          allCardsImageViews.get(position.getY())
              .setOnMouseClicked(createClickOnCardHandler(gameId, allActions));
        }
      }
    }
  }


  @Override
  public void setup() {
    setDeckLevelText();
    setUpCards(cards);
  }

}
