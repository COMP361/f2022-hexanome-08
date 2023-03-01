package project.view.splendor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import project.CardActionController;
import project.DeckActionController;

/**
 * Gui class that represents a level of cards.
 */
public class BaseCardLevelGui extends HBox implements DevelopmentCardBoardGui {

  private final int level;
  private List<BaseCard> cards;
  private List<BaseCard> deck;

  /**
   * Constructor of BaseCardLevelGui class.
   *
   * @param level level of the cards (1,2,3)
   * @param cards a list of cards (fixed length of 4)
   * @param deck a list of cards (change length based on level)
   */
  public BaseCardLevelGui(int level, List<BaseCard> cards, List<BaseCard> deck) {
    this.level = level;
    this.cards = cards;
    this.deck = deck;
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

  private void setUpCards(List<BaseCard> cards) {
    assert cards.size() == 4;
    int i = 0;
    for (BaseCard card : cards) {
      String curCardName = card.getCardName();
      int curCardLevel = card.getLevel();
      String cardPath =
          String.format("project/pictures/level%d/%s.png", curCardLevel, curCardName);
      Image cardImg = new Image(cardPath);
      getOneCardGui(i).setImage(cardImg);
      i += 1;
    }

  }

  private EventHandler<MouseEvent> createClickOnCardHandler(long gameId, String[] actionHash) {
    return event -> {
      try {
        App.loadPopUpWithController("card_action.fxml",
            new CardActionController(gameId, actionHash), 360, 170);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }

  private EventHandler<MouseEvent> createClickOnDeckHandler() {
    return event -> {
      try {
        App.loadPopUpWithController("deck_action.fxml",
            new DeckActionController(), 360, 170);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }

  @Override
  public void bindActionToCardAndDeck(String[][] actionHashLookUp, long gameId) {
    // get all cards first
    List<ImageView> allCards = getAllCardsGui();
    for (int i = 0; i < allCards.size(); i++) {
      String[] actionHashOptions = actionHashLookUp[i];
      allCards.get(i).setOnMouseClicked(createClickOnCardHandler(gameId, actionHashOptions));
    }

    Group deck = (Group) this.getChildren().get(0);
    deck.setOnMouseClicked(createClickOnDeckHandler());
  }

  private void setDeckLevelText() {
    Group levelCard = (Group) this.getChildren().get(0);
    Rectangle rectangle = (Rectangle) levelCard.getChildren().get(0);
    int newRemainingCards = deck.size();
    if (level == 3) {
      rectangle.setFill(Color.DODGERBLUE);
      Text deck = (Text) levelCard.getChildren().get(1);
      Text levelOfCard = (Text) levelCard.getChildren().get(2);
      deck.setText(newRemainingCards + "");
      deck.setFont(Font.font("System", FontPosture.REGULAR, 16));
      levelOfCard.setText(". . .");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 18));
    } else if (level == 2) {
      rectangle.setFill(Color.YELLOW);
      Text deck = (Text) levelCard.getChildren().get(1);
      Text levelOfCard = (Text) levelCard.getChildren().get(2);
      deck.setText(newRemainingCards + "");
      deck.setFont(Font.font("System", FontPosture.REGULAR, 16));
      levelOfCard.setText(" . .");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 18));
    } else if (level == 1) {
      rectangle.setFill(Paint.valueOf("#30ff1f"));
      Text deck = (Text) levelCard.getChildren().get(1);
      Text levelOfCard = (Text) levelCard.getChildren().get(2);
      deck.setText(newRemainingCards + "");
      deck.setFont(Font.font("System", FontPosture.REGULAR, 16));
      levelOfCard.setText(" .");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 18));
    }
  }

  public void setCards(List<BaseCard> cards) {
    this.cards = cards;
  }

  public void setDeck(List<BaseCard> deck) {
    this.deck = deck;
  }

  @Override
  public void setup() {
    setDeckLevelText();
    setUpCards(cards);
  }
}
