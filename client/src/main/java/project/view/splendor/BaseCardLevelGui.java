package project.view.splendor;

import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
import ca.mcgill.comp361.splendormodel.model.Position;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import project.App;
import project.controllers.popupcontrollers.CardActionController;
import project.controllers.popupcontrollers.DeckActionController;

/**
 * Gui class that represents a level of cards.
 */
public class BaseCardLevelGui extends HBox implements DevelopmentCardBoardGui {

  private final int level;
  private final Rectangle coverRectangle;
  private DevelopmentCard[] cards;
  private List<DevelopmentCard> deck;

  /**
   * Constructor of BaseCardLevelGui class.
   *
   * @param level level of the cards (1,2,3)
   * @param cards a list of cards (fixed length of 4)
   * @param deck  a list of cards (change length based on level)
   */
  public BaseCardLevelGui(int level, DevelopmentCard[] cards, List<DevelopmentCard> deck,
                          Rectangle coverRectangle) {
    this.level = level;
    this.cards = cards;
    this.deck = deck;
    this.coverRectangle = coverRectangle;
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/project/base_card_template.fxml"));
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
    for (int i = 0; i < 4; i++) {
      ImageView curImageView = (ImageView) this.getChildren().get(i + 1);
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
      String cardPath = App.getBaseCardPath(curCardName, curCardLevel);
      Image cardImg = new Image(cardPath);
      getOneCardGui(i).setImage(cardImg);
    }
  }

  /**
   * This method creates a CardActionController for a set of specific actions [so a pop up
   * can appear to display to the user those actions].
   *
   * @param gameId     ID of current game
   * @param allActions list of Actions which you will be pairing to a card *using "clickOn" event.
   * @return Returns a list of Event Handlers. (event type is a pop up when clicked on).
   */
  public EventHandler<MouseEvent> createClickOnCardHandler(long gameId,
                                                           List<ActionIdPair> allActions) {
    return event -> {
      App.loadPopUpWithController("card_action.fxml",
          new CardActionController(gameId, allActions, null), 360, 170);
    };
  }

  private EventHandler<MouseEvent> createClickOnDeckHandler(long gameId, String actionId) {
    return event -> {
      App.loadPopUpWithController("deck_action.fxml",
          new DeckActionController(gameId, actionId, coverRectangle), 360, 170);
    };
  }


  private void setDeckLevelText() {
    Group levelCard = (Group) this.getChildren().get(0);
    Rectangle rectangle = (Rectangle) levelCard.getChildren().get(0);
    int newRemainingCards = deck.size();
    Text deck = (Text) levelCard.getChildren().get(1);
    Text levelOfCard = (Text) levelCard.getChildren().get(2);
    if (level == 3) {
      rectangle.setFill(Color.DODGERBLUE);
      deck.setText(newRemainingCards + "");
      deck.setFont(Font.font("System", FontPosture.REGULAR, 16));
      levelOfCard.setText(". . .");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 18));
    } else if (level == 2) {
      rectangle.setFill(Color.YELLOW);
      deck.setText(newRemainingCards + "");
      deck.setFont(Font.font("System", FontPosture.REGULAR, 16));
      levelOfCard.setText(" . .");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 18));
    } else if (level == 1) {
      rectangle.setFill(Paint.valueOf("#30ff1f"));
      deck.setText(newRemainingCards + "");
      deck.setFont(Font.font("System", FontPosture.REGULAR, 16));
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
  // set up actions to this level gui
  public void bindActionToCardAndDeck(Map<Position, List<ActionIdPair>> positionToActionMap,
                                      long gameId) {
    Map<Position, List<ActionIdPair>> curLevelMap = positionToActionMap.entrySet()
        .stream().filter(e -> e.getKey().getX() == level)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    List<ImageView> allCards = getAllCardsGui();
    for (Position position : curLevelMap.keySet()) {
      if (position.getY() == -1) {
        // assign reserve action to deck (deck is only clickable if player has such action)
        Group deck = (Group) this.getChildren().get(0);
        ActionIdPair actionIdPair = curLevelMap.get(position).get(0);
        String actionId = actionIdPair.getActionId();
        deck.setOnMouseClicked(createClickOnDeckHandler(gameId, actionId));
      } else {
        List<ActionIdPair> allActions = curLevelMap.get(position);
        allCards.get(position.getY())
            .setOnMouseClicked(createClickOnCardHandler(gameId, allActions));
      }
    }
  }


  @Override
  public void setup() {
    setDeckLevelText();
    setUpCards(cards);
  }
}
