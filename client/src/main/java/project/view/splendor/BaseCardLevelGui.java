package project.view.splendor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.HBox;


import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;

public class BaseCardLevelGui extends HBox {

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

    public double getBaseCardSpace() {
        return level;
    }


    public void setup(){
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
}
