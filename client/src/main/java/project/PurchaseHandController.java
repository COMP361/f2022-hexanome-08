package project;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import project.view.splendor.Colour;

public class PurchaseHandController implements Initializable {

  @FXML
  private HBox cardStackHbox;

  private final Map<Colour, Group> colourGroupMap;
  private final Map<Colour, List<DevelopmentCard>> colourCardsMap;

  public PurchaseHandController(Map<Colour, List<DevelopmentCard>> colourCardsMap) {
    this.colourGroupMap = new HashMap<>();
    this.colourCardsMap = colourCardsMap;
  }

  private List<HBox> generateCardSatchelPair(List<DevelopmentCard> oneColourCards) {
    List<HBox> result = new ArrayList<>();
    for (DevelopmentCard card : oneColourCards) {
      Rectangle satchelMark = new Rectangle();
      // TODO: Colour will be assigned differently if the card is linked
      if (card.isPaired()) {
        satchelMark.setFill(Color.BLUE);
      } else {
        satchelMark.setFill(Color.WHITESMOKE);
      }
      Image img;
      // TODO: check the card type in some way in the future...
      img = new Image(App.getBaseCardPath(card.getCardName(), card.getLevel()));
      //if (card instanceof DevelopmentCard) {
      //  img = new Image(App.getBaseCardPath(card.getCardName(), card.getLevel()));
      //} else {
      //  img = new Image(App.getOrientCardPath(card.getCardName(), card.getLevel()));
      //}
      ImageView imgV = new ImageView(img);
      imgV.setFitWidth(100);
      imgV.setFitHeight(150);
      satchelMark.setWidth(20);
      satchelMark.setHeight(150);
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
      n.setLayoutX(i * 10);
      n.setLayoutY(i * 20);
      i += 1;
    }
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Colour[] baseColours = App.getBaseColours();
    // assigning colour to each group
    for (int i = 0; i < 5; i++) {
      Colour curColour = baseColours[i];
      Group colourGroup = (Group) cardStackHbox.getChildren().get(i);
      colourGroupMap.put(curColour, colourGroup);
    }
    // TODO: Send request and get the list of cards the player has and make it
    // (currently, HARDCODED!)

    for (Colour c : colourGroupMap.keySet()) {
      if (colourCardsMap.containsKey(c)) {
        List<DevelopmentCard> cardsOfOneColour = colourCardsMap.get(c);
        List<HBox> allPairs = generateCardSatchelPair(cardsOfOneColour);
        addCardSatchelPairToColourGroup(allPairs, colourGroupMap.get(c));
      }
    }

    //for (Group colourGroup : colourGroupMap.values()) {
    //  // TODO: Replace these images later from server response
    //  List<ImageView> allImageViews = new ArrayList<>();
    //  for (int i = 0; i < 3; i++) {
    //    Image img = new Image(App.getBaseCardPath("b1", 2));
    //    ImageView imgV = new ImageView(img);
    //    allImageViews.add(imgV);
    //  }
    //  List<HBox> allPairs = generateCardSatchelPair(allImageViews);
    //  addCardSatchelPairToColourGroup(allPairs, colourGroup);
    //}
    //
    //VBox orientGoldTokenCardVbox = (VBox) cardStackHbox.getChildren().get(5);
    //// initializing the GUI of the gold token dev card in ORIENT
    //for (int i = 0; i < 3; i++) {
    //  Image img = new Image(App.getOrientCardPath("o1b1", 1));
    //  ImageView imgV = new ImageView(img);
    //  imgV.setFitHeight(150);
    //  imgV.setFitWidth(100);
    //  orientGoldTokenCardVbox.getChildren().add(imgV);
    //}

  }
}
