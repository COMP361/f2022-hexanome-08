package project.view.splendor;

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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import project.App;
import project.view.splendor.gameitems.DevelopmentCard;

public class OrientCardLevelGui extends HBox implements DevelopmentCardBoardGui {
    private final int level;
    public OrientCardLevelGui(int level){
        this.level = level;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/orient_card_template.fxml"));
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

    private void setDeckLevelText() {
        Group levelCard = (Group) this.getChildren().get(2);
        Rectangle rectangle = (Rectangle) levelCard.getChildren().get(0);
        rectangle.setFill(Color.RED);
        Text deck = (Text) levelCard.getChildren().get(1);
        Text levelOfCard = (Text) levelCard.getChildren().get(2);
        deck.setText("10");
        if(this.level == 3){
            levelOfCard.setText(". . .");
        } else if(this.level == 2){
            levelOfCard.setText(". .");
        } else if(this.level == 1){
            levelOfCard.setText(".");
        }
    }

    private void setUpCards(List<DevelopmentCard> cards) {
        assert cards.size() == 2;
        int i = 0;
        for (DevelopmentCard card : cards) {
            String curCardName = card.getCardName();
            int curCardLevel = card.getLevel();
            String cardPath =
                String.format("project/pictures/orient/%d/%s.png", curCardLevel, curCardName);
            Image cardImg = new Image(cardPath);
            getOneCardGui(i).setImage(cardImg);
            i += 1;
        }

    }

    private EventHandler<MouseEvent> createClickOnCardHandler() {
        return event -> {
            try {
                App.setRootWithSizeTitle("splendor_card_action",
                    360, 170, "Make your decision");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private EventHandler<MouseEvent> createClickOnDeckHandler() {
        return event -> {
            try {
                App.setRootWithSizeTitle("splendor_deck_action",
                    360, 170, "Make your decision");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
    private void bindActionToCardAndDeck() {
        // get all cards first
        List<ImageView> allCards = getAllCardsGui();

        for (ImageView imgV : allCards) {
            imgV.setOnMouseClicked(createClickOnCardHandler());
        }

        Group deck = (Group) this.getChildren().get(2);
        deck.setOnMouseClicked(createClickOnDeckHandler());
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


    @Override
    public void setup(List<DevelopmentCard> cards) {
        setDeckLevelText();
        setUpCards(cards);
        bindActionToCardAndDeck();
    }

}
