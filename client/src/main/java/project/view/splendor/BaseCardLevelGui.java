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

public class BaseCardLevelGui extends HBox implements DevelopmentCardBoardGui {

    private final int level;
    public BaseCardLevelGui(int level){
        this.level = level;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/base_card_template.fxml"));
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
            ImageView curImageView = (ImageView) this.getChildren().get(i+1);
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

        Group deck = (Group) this.getChildren().get(0);
        deck.setOnMouseClicked(createClickOnDeckHandler());
    }

    private void setDeckLevelText() {
        Group levelCard = (Group) this.getChildren().get(0);
        Rectangle rectangle = (Rectangle) levelCard.getChildren().get(0);
        if(this.level == 3){
            rectangle.setFill(Color.BLUE);
            Text deck = (Text) levelCard.getChildren().get(1);
            Text levelOfCard = (Text) levelCard.getChildren().get(2);
            deck.setText("20");
            levelOfCard.setText(". . .");
        } else if(this.level == 2){
            rectangle.setFill(Color.YELLOW);
            Text deck = (Text) levelCard.getChildren().get(1);
            Text levelOfCard = (Text) levelCard.getChildren().get(2);
            deck.setText("30");
            levelOfCard.setText(". .");
        } else if(this.level == 1){
            rectangle.setFill(Color.GREEN);
            Text deck = (Text) levelCard.getChildren().get(1);
            Text levelOfCard = (Text) levelCard.getChildren().get(2);
            deck.setText("40");
            levelOfCard.setText(".");
        }
    }

    @Override
    public void setup(List<DevelopmentCard> cards){
        setDeckLevelText();
        setUpCards(cards);
        bindActionToCardAndDeck();

    }
}
