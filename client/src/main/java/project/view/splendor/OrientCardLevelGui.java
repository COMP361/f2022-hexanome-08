package project.view.splendor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;

public class OrientCardLevelGui extends HBox {
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

    public void setup(){
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
}
