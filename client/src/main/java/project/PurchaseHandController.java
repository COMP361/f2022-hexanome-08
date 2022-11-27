package project;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import project.view.splendor.Colour;

public class PurchaseHandController {

  @FXML
  private HBox cardStackHbox;

  private final Map<Colour, Group> colourGroupMap = new HashMap<>();

  private List<HBox> generateCardSatchelPair(List<ImageView> allImageViews){
    List<HBox> result = new ArrayList<>();
    for (ImageView imgV : allImageViews) {
      Rectangle satchelMark = new Rectangle();
      // TODO: Colour will be assigned differently if the card is linked
      //satchelMark.setFill(Color.WHITESMOKE);
      satchelMark.setFill(Color.BLUE);
      satchelMark.setWidth(20);
      satchelMark.setHeight(150);
      imgV.setFitWidth(100);
      imgV.setFitHeight(150);
      HBox container = new HBox(imgV, satchelMark);
      result.add(container);
    }
    return result;
  }

  private void addCardSatchelPairToColourGroup(
      List<HBox> cardSatchelPairs, Group groupOfOneColour) {

    for (HBox pair : cardSatchelPairs) {
      groupOfOneColour.getChildren().add(pair);
    }

    int i = 0;
    for (Node n : groupOfOneColour.getChildren()) {
      n.setLayoutX(i*10);
      n.setLayoutY(i*20);
      i+=1;
    }
  }

  public void initialize() {
    Colour[] baseColours = App.getBaseColours();



    // assigning colour to each group
    for (int i = 0; i < 5; i++) {
      Colour curColour = baseColours[i];
      Group colourGroup = (Group) cardStackHbox.getChildren().get(i);
      colourGroupMap.put(curColour, colourGroup);
    }
    // TODO: Send request and get the list of cards the player has and make it
    // (currently, HARDCODED!)
    for (Group colourGroup : colourGroupMap.values()) {
      // TODO: Replace these images later from server response
      List<ImageView> allImageViews = new ArrayList<>();
      for (int i = 0; i < 3; i++) {
        Image img = new Image(String.format("project/pictures/level2/b1.png"));
        ImageView imgV = new ImageView(img);
        allImageViews.add(imgV);
      }
      List<HBox> allPairs = generateCardSatchelPair(allImageViews);
      addCardSatchelPairToColourGroup(allPairs, colourGroup);
    }

    VBox orientGoldTokenCardVbox = (VBox) cardStackHbox.getChildren().get(5);
    // initializing the GUI of the gold token dev card in ORIENT
    for (int i = 0; i < 3; i++) {
      Image img = new Image("project/pictures/orient/1/o1b1.png");
      ImageView imgV = new ImageView(img);
      imgV.setFitHeight(150);
      imgV.setFitWidth(100);
      orientGoldTokenCardVbox.getChildren().add(imgV);
    }

  }
}
