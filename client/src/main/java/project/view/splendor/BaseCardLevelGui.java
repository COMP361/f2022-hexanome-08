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

public class BaseCardLevelGui extends HBox implements DevelopmentCardBoardGui {

  private final int level;
  private final List<DevelopmentCard> cards;

  public BaseCardLevelGui(int level, List<DevelopmentCard> cards) {
    this.level = level;
    this.cards = cards;
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

  private void setUpCards(List<DevelopmentCard> cards) {
    assert cards.size() == 4;
    int i = 0;
    for (DevelopmentCard card : cards) {
      String curCardName = card.getCardName();
      int curCardLevel = card.getLevel();
      String cardPath =
          String.format("project/pictures/level%d/%s.png", curCardLevel, curCardName);
      Image cardImg = new Image(cardPath);
      getOneCardGui(i).setImage(cardImg);
      i += 1;
    }

  }

  private EventHandler<MouseEvent> createClickOnCardHandler(DevelopmentCard curCard) {
    return event -> {
      try {
        App.loadPopUpWithController("card_action.fxml",
            new CardActionController(curCard), 360, 170);
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

  private void bindActionToCardAndDeck() {
    // get all cards first
    List<ImageView> allCards = getAllCardsGui();

    for (int i = 0; i < allCards.size(); i++) {
      DevelopmentCard curCard = cards.get(i);
      allCards.get(i).setOnMouseClicked(createClickOnCardHandler(curCard));
    }

    Group deck = (Group) this.getChildren().get(0);
    deck.setOnMouseClicked(createClickOnDeckHandler());
  }

  private void setDeckLevelText() {
    Group levelCard = (Group) this.getChildren().get(0);
    Rectangle rectangle = (Rectangle) levelCard.getChildren().get(0);
    if (this.level == 3) {
      rectangle.setFill(Color.DODGERBLUE);
      Text deck = (Text) levelCard.getChildren().get(1);
      Text levelOfCard = (Text) levelCard.getChildren().get(2);
      deck.setText("20");
      deck.setFont(Font.font("System", FontPosture.REGULAR, 16));
      levelOfCard.setText(". . .");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 18));
    } else if (this.level == 2) {
      rectangle.setFill(Color.YELLOW);
      Text deck = (Text) levelCard.getChildren().get(1);
      Text levelOfCard = (Text) levelCard.getChildren().get(2);
      deck.setText("30");
      deck.setFont(Font.font("System", FontPosture.REGULAR, 16));
      levelOfCard.setText(" . .");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 18));
    } else if (this.level == 1) {
      rectangle.setFill(Paint.valueOf("#30ff1f"));
      Text deck = (Text) levelCard.getChildren().get(1);
      Text levelOfCard = (Text) levelCard.getChildren().get(2);
      deck.setText("40");
      deck.setFont(Font.font("System", FontPosture.REGULAR, 16));
      levelOfCard.setText(" .");
      levelOfCard.setFont(Font.font("System", FontPosture.REGULAR, 18));
    }
  }

  @Override
  public void setup() {
    setDeckLevelText();
    setUpCards(cards);
    bindActionToCardAndDeck();

  }
}
