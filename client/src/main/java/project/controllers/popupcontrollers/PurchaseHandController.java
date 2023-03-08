package project.controllers.popupcontrollers;


import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.model.Colour;
import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
import ca.mcgill.comp361.splendormodel.model.NobleCard;
import ca.mcgill.comp361.splendormodel.model.PurchasedHand;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import project.App;

/**
 * Purchase controller class, controls the visual updates happening in purchase hand.
 * associated with "my_development_cards.fxml"
 */
public class PurchaseHandController implements Initializable {

  private final Map<Colour, Group> colourGroupMap;
  private final Map<Colour, List<DevelopmentCard>> colourCardsMap;
  private final List<NobleCard> nobleCards;
  private final Map<String, Action> playerActions;
  private final Rectangle coverRectangle;
  @FXML
  // each index represent the group of displaying all cards of one colour
  // there are 6 colours to display (hold the Groups)
  private HBox cardStackHbox;
  @FXML
  // place to store the gold token cards.
  // hold the Gold->List<DevCards>
  private VBox goldTokenCardsVbox;
  @FXML
  // hold List<NobleCard>
  private VBox noblesUnLocked;

  /**
   * PurchaseHandController.
   *
   * @param purchasedHand purchasedHand
   * @param playerActions playerActions
   * @param coverRectangle coverRectangle
   */
  public PurchaseHandController(PurchasedHand purchasedHand, Map<String, Action> playerActions,
                                Rectangle coverRectangle) {
    // organize all dev cards (including gold colour ones) into colour map
    List<DevelopmentCard> allCardsInHand = purchasedHand.getDevelopmentCards();
    this.colourCardsMap = reorganizeCardsInHand(allCardsInHand);
    this.colourGroupMap = new HashMap<>();
    this.nobleCards = purchasedHand.getNobleCards();
    this.playerActions = playerActions;
    this.coverRectangle = coverRectangle;
  }


  /**
   * Put cards in different groups based on colour type.
   * The colour can be gold.
   *
   * @param allDevCards allDevCards
   * @return return a map
   */
  private Map<Colour, List<DevelopmentCard>> reorganizeCardsInHand(
      List<DevelopmentCard> allDevCards) {
    Map<Colour, List<DevelopmentCard>> result = new HashMap<>();
    for (DevelopmentCard card : allDevCards) {
      if (!result.containsKey(card.getGemColour())) {
        // initialize the list for cards
        List<DevelopmentCard> cardsOfOneColour = new ArrayList<>();
        cardsOfOneColour.add(card);
        result.put(card.getGemColour(), cardsOfOneColour);
      } else {
        // if result contains this colour before, then we just
        // need to add this card to the list the colour maps to
        //TODO: We need to sort them LATER!!!!
        result.get(card.getGemColour()).add(card);
      }
    }
    return result;
  }


  // Add the satchel mark display to every dev card (exclude the orient gold card)
  private List<HBox> generateCardSatchelPair(List<DevelopmentCard> oneColourCards) {
    List<HBox> result = new ArrayList<>();
    for (DevelopmentCard card : oneColourCards) {
      Rectangle satchelMark = new Rectangle();
      // Colour will be assigned differently if the card is linked
      if (card.isPaired()) {
        satchelMark.setFill(Color.BLUE);
      } else {
        satchelMark.setFill(Color.WHITESMOKE);
      }
      Image img;
      // has some effect -> orient
      if (card.getPurchaseEffects().size() > 0) {
        img = new Image(App.getOrientCardPath(card.getCardName(), card.getLevel()));
      } else { // otherwise, base
        img = new Image(App.getBaseCardPath(card.getCardName(), card.getLevel()));
      }
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

  // display it a bit inclined
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

  private void addGoldTokenCards(List<DevelopmentCard> goldTokenCards, VBox goldTokenCardsVbox) {
    if (goldTokenCards != null && goldTokenCards.size() > 0) {
      for (DevelopmentCard card : goldTokenCards) {
        String cardPath = App.getOrientCardPath(card.getCardName(), card.getLevel());
        Image image = new Image(cardPath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(150);
        // add the image view to vbox
        goldTokenCardsVbox.getChildren().add(imageView);
      }
    }
  }

  private void addNobleCards(List<NobleCard> nobleCards, VBox noblesUnLocked) {
    for (NobleCard nobleCard : nobleCards) {
      String noblePath = App.getNoblePath(nobleCard.getCardName());
      ImageView imageView = new ImageView(new Image(noblePath));
      // add the image view to vbox
      noblesUnLocked.getChildren().add(imageView);
    }
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Colour[] baseColours = App.getBaseColours();
    // the groups are stored in HBox in FXML file, there are 6 elements in the HBox
    // the first 5 are groups that store regular dev cards
    // the 6th element is the VBox storing gold orient cards
    for (int i = 0; i < 5; i++) {
      Colour curColour = baseColours[i];
      Group colourGroup = (Group) cardStackHbox.getChildren().get(i);
      // store a colour map look up reference to the GUI group so that it
      // is easier to have access to it rather than counting the index from 0 to 4
      colourGroupMap.put(curColour, colourGroup);
    }

    // add the gold colour cards' image views to vbox
    List<DevelopmentCard> goldTokenCards = colourCardsMap.get(Colour.GOLD);
    addGoldTokenCards(goldTokenCards, goldTokenCardsVbox);

    for (Colour c : colourGroupMap.keySet()) {
      if (colourCardsMap.containsKey(c)) {
        List<DevelopmentCard> cardsOfOneColour = colourCardsMap.get(c);
        //TODO: assign actions to image views with playerActions, depending on what kind of
        // actions (only CardExtraAction of Satchel can happen in this purchase hand,
        // only for normal
        // cards with RED, WHITE, BLUE, GREEN, BLACK colours)
        // Bind actions to image view during generateCardSatchelPair(...) method
        List<HBox> allPairs = generateCardSatchelPair(cardsOfOneColour);
        addCardSatchelPairToColourGroup(allPairs, colourGroupMap.get(c));
      }
    }

    // display the noble cards to GUI
    addNobleCards(nobleCards, noblesUnLocked);

  }
}