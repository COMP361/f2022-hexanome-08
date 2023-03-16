package project.view.splendor;

import ca.mcgill.comp361.splendormodel.actions.ReturnTokenAction;
import ca.mcgill.comp361.splendormodel.actions.TakeTokenAction;
import ca.mcgill.comp361.splendormodel.model.Colour;
import ca.mcgill.comp361.splendormodel.model.SplendorDevHelper;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import project.App;
import project.connection.GameRequestSender;

/**
 * Create the GUI for the bank.
 */
public class TokenBankGui extends HBox {

  private final long gameId;
  private Map<String, TakeTokenAction> takeTokenActionMap = new HashMap<>();
  private Map<String, ReturnTokenAction> returnTokenActionMap = new HashMap<>();

  /**
   * Construct the Token Bank GUI.
   */
  public TokenBankGui(long gameId) {
    this.gameId = gameId;
    // TODO: The fxml associated with this class, must be bind to controller = project.App
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/token_bank.fxml"));
    fxmlLoader.setRoot(this);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Button getConfirmButton() {
    ObservableList<Node> allChildren = this.getChildren();
    VBox confirmVbox = (VBox) allChildren.get(allChildren.size() - 1);
    return (Button) confirmVbox.getChildren().get(1);
  }

  //private EventHandler<ActionEvent> createTakeTokenHandler(
  //    Label displayZone, Colour curColour) {
  //  return event -> {
  //    // add only if it's a valid take
  //    // for this colour
  //    // and for all colour
  //    int curNum = Integer.parseInt(displayZone.getText());
  //    int tokenLeft = Integer.parseInt(this.getColourTokenBankMap().get(curColour).getText());
  //    // can only increment if 0 <= takeNum < 2 && bank > 0
  //
  //    int uniqueColourTakenCount = 0;
  //    for (Colour c : this.getTakeTokenDecision().keySet()) {
  //      int curColourTake = this.getTakeTokenDecision().get(c);
  //      if (curColourTake > 0) {
  //        uniqueColourTakenCount += 1;
  //      }
  //    }
  //
  //    if (curNum == 0 && uniqueColourTakenCount == 2) {
  //      curNum += 1;
  //      String newNum = curNum + "";
  //      displayZone.setText(newNum);
  //    } else {
  //      if (!(curNum == 1 && uniqueColourTakenCount == 2)
  //          && curNum < 2 && curNum >= 0 && tokenLeft > 0
  //          && !banFromTaking(uniqueColourTakenCount)) {
  //        // then we can have this Label being changed
  //        if (curNum == 1) {
  //          if (tokenLeft >= 4) {
  //            curNum += 1;
  //            String newNum = curNum + "";
  //            displayZone.setText(newNum);
  //          }
  //        } else {
  //          curNum += 1;
  //          String newNum = curNum + "";
  //          displayZone.setText(newNum);
  //        }
  //
  //      }
  //    }
  //    uniqueColourTakenCount = 0;
  //    for (Colour c : this.getTakeTokenDecision().keySet()) {
  //      int curColourTake = this.getTakeTokenDecision().get(c);
  //      if (curColourTake > 0) {
  //        uniqueColourTakenCount += 1;
  //      }
  //    }
  //    if (banFromTaking(uniqueColourTakenCount)) {
  //      getConfirmButton().setDisable(false);
  //    }
  //  };
  //}


  private EventHandler<ActionEvent> createConfirmTakeTokenHandler(String actionId) {
    // TODO: Need to send the request to Game server for future use
    return event -> {
      GameRequestSender sender = App.getGameRequestSender();
      String playerName = App.getUser().getUsername();
      String accessToken = App.getUser().getAccessToken();
      sender.sendPlayerActionChoiceRequest(gameId, playerName, accessToken, actionId);
      //Map<Colour, Text> bankTokenBankMap = getColourTokenBankMap();
      //Map<Colour, Integer> playerDecision = getTakeTokenDecision();
      Map<Colour, Label> playerNewDecisions = getColourTokenTakeMap();
      for (Colour c : playerNewDecisions.keySet()) {
        if (!c.equals(Colour.GOLD)) {
          //Text bankBalanceText = bankTokenBankMap.get(c);
          Label playerNewChoice = playerNewDecisions.get(c);
          //int curTokenLeft = Integer.parseInt(bankBalanceText.getText());
          //int curTokenTake = playerDecision.get(c);
          //int newBankBalance = curTokenLeft - curTokenTake;
          // reset decision back to 0
          playerNewChoice.setText("0");
          //bankBalanceText.setText(newBankBalance + "");
        }
      }
      // disable
      getConfirmButton().setDisable(true);
    };

  }


  // check whether is a valid take over all different colours
  // use for confirmation button
  private boolean banFromTaking(int uniqueColourTakenCount) {
    // 3 of each different colour
    //      1. if the colour has remaining (>0)
    // 2 of same colour:
    //      1. if the colour has remaining (>0)
    //      2. if the colour has at least 4 left
    int totalTokenTakeCount = 0;
    for (Colour c : this.getTakeTokenDecision().keySet()) {
      int curColourTake = this.getTakeTokenDecision().get(c);
      if (curColourTake > 0) {
        totalTokenTakeCount += curColourTake;
      }
    }

    if (uniqueColourTakenCount == 3) {
      return totalTokenTakeCount == 3;
    } else if (uniqueColourTakenCount == 1) {
      return totalTokenTakeCount == 2;
    } else {
      return false;
    }
  }

  // the Label stores the number of player current take token decision (contains no gold colour)
  private Map<Colour, Label> getColourTokenTakeMap() {
    // processing token number Text of 5 regular token
    Map<Colour, Label> resultMap = new HashMap<>();
    ObservableList<Node> allChildren = this.getChildren();
    Colour[] colours = App.getBaseColours();
    for (int i = 0; i < 5; i++) {
      Group curGroup = (Group) allChildren.get(i);
      Label takeTokenNum = (Label) curGroup.getChildren().get(curGroup.getChildren().size() - 2);
      resultMap.put(colours[i], takeTokenNum);
    }
    return resultMap;
  }

  // return a map of player's take token decision in colour -> int format
  private Map<Colour, Integer> getTokensTakenDecision() {
    Map<Colour, Integer> result = new HashMap<>();
    Map<Colour, Label> tokensTakenInLabel = getColourTokenTakeMap();
    for (Colour colour : tokensTakenInLabel.keySet()) {
      int amount = Integer.parseInt(tokensTakenInLabel.get(colour).getText());
      result.put(colour, amount);
    }
    result.put(Colour.GOLD, 0);
    return result;
  }

  /**
   * Return the bank balance of each token left in bank (including gold).
   *
   * @return a map from colour to the GUI object Text containing the bank balance
   */
  public Map<Colour, Text> getColourTokenBankMap() {
    Map<Colour, Text> resultTextList = new HashMap<>();
    ObservableList<Node> allChildren = this.getChildren();
    Colour[] colours = App.getAllColours();
    // processing token number Text of 5 regular token
    for (int i = 0; i < 5; i++) {
      Group curGroup = (Group) allChildren.get(i);
      Text curTokenNum = (Text) curGroup.getChildren().get(curGroup.getChildren().size() - 1);
      resultTextList.put(colours[i], curTokenNum);
    }

    // processing the gold token number Text
    VBox goldToken = (VBox) allChildren.get(5);
    Group goldTokenGroup = (Group) goldToken.getChildren().get(1);
    Text curTokenNum = (Text) goldTokenGroup.getChildren().get(1);
    resultTextList.put(colours[5], curTokenNum);
    return resultTextList;
  }

  // display the bank balance correctly
  private void setColourTokenBankMap(Map<Colour, Integer> bankBalanceMap) {
    Map<Colour, Text> curBankTokenBankMap = getColourTokenBankMap();
    Colour[] allColours = App.getAllColours();
    for (Colour c : allColours) {
      Text curText = curBankTokenBankMap.get(c);
      int newBalance = bankBalanceMap.get(c);
      curText.setText(newBalance + "");
    }
  }

  /**
   * Get the take token decision of user.
   *
   * @return a map from colour to the GUI object Text containing the user decision
   */
  private EnumMap<Colour, Integer> getTakeTokenDecision() {
    EnumMap<Colour, Integer> result = SplendorDevHelper.getInstance().getRawTokenColoursMap();
    for (Colour c : this.getColourTokenTakeMap().keySet()) {
      int curTake = Integer.parseInt(this.getColourTokenTakeMap().get(c).getText());
      result.put(c, curTake);
    }
    return result;
  }


  // return "" if found no action id matching
  private String tokenCombMatchedActionId() {
    EnumMap<Colour, Integer> currentChoiceComb = getTakeTokenDecision();
    if (takeTokenActionMap.size() > 0) {
      for (String actionId : takeTokenActionMap.keySet()) {
        TakeTokenAction takeTokenAction = takeTokenActionMap.get(actionId);
        EnumMap<Colour, Integer> comb = takeTokenAction.getTokens();
        // if any combination matches,
        if (comb.equals(currentChoiceComb)) {
          //System.out.println("Action Id:" + actionId);
          //System.out.println("Current choice: " + comb);
          //System.out.println("Action Combo: " + currentChoiceComb);
          return actionId;
        }
      }
    } else if (returnTokenActionMap.size() > 0) { //check for matches with existing return actions
      for (String actionId : returnTokenActionMap.keySet()) {
        ReturnTokenAction returnTokenAction = returnTokenActionMap.get(actionId);
        EnumMap<Colour, Integer> comb = returnTokenAction.getTokens();
        // if any combination matches,
        System.out.println("Action Id:" + actionId);
        System.out.println("Current choice: " + comb);
        System.out.println("Action Combo: " + currentChoiceComb);

        if (comb.equals(currentChoiceComb)) {
          return actionId;
        }
      }
    }
    // can return more
    //if (returnTokenActionMap.size() > 0) {
    //  for (String actionId : returnTokenActionMap.keySet()) {
    //    ReturnTokenAction returnTokenAction = returnTokenActionMap.get(actionId);
    //    EnumMap<Colour, Integer> comb = returnTokenAction.getTokens();
    //    // if any combination matches,
    //    if (comb.equals(currentChoiceComb)) {
    //      return actionId;
    //    }
    //  }
    //}
    return "";
  }


  private EventHandler<ActionEvent> createBackTokenHandler(Label displayZone) {
    return event -> {
      int curNum = Integer.parseInt(displayZone.getText());
      // can always decrease as long as > 0
      if (curNum > 0) {
        // then we can have this Label being changed
        curNum -= 1;
        String newNum = curNum + "";
        getConfirmButton().setDisable(true);
        displayZone.setText(newNum);
      }
    };
  }

  private EventHandler<ActionEvent> createTakeTokenHandler(Label displayZone) {
    return event -> {

      int curNum = Integer.parseInt(displayZone.getText());
      if (tokenCombMatchedActionId().equals("")) {
        // means we did not find a match yet, allow user to keep taking
        curNum += 1;
        String newNum = curNum + "";
        displayZone.setText(newNum);
        // the after effect of +1
        if (!tokenCombMatchedActionId().equals("")) {
          Button confirmButton = getConfirmButton();
          // do not allow taking anymore, activate confirm button
          confirmButton.setDisable(false);
          String actionId = tokenCombMatchedActionId();
          confirmButton.setOnAction(createConfirmTakeTokenHandler(actionId));
        }
      } else {
        Button confirmButton = getConfirmButton();
        // do not allow taking anymore, activate confirm button
        confirmButton.setDisable(false);
        String actionId = tokenCombMatchedActionId();
        confirmButton.setOnAction(createConfirmTakeTokenHandler(actionId));
      }


    };
  }

  /**
   * Bind all functionality to the GUI objects we care.
   */
  private void bindActionToButtonAndLabel() {
    ObservableList<Node> allChildren = this.getChildren();
    Colour[] colours = App.getBaseColours();
    // iterate through all five regular tokens
    for (int i = 0; i < 5; i++) {
      Group curGroup = (Group) allChildren.get(i);
      Colour curColour = colours[i];
      Button plusButton = (Button) curGroup.getChildren().get(1);
      Button minusButton = (Button) curGroup.getChildren().get(2);
      Label displayLabel = (Label) curGroup.getChildren().get(curGroup.getChildren().size() - 2);
      if (takeTokenActionMap.isEmpty() && returnTokenActionMap.isEmpty()) {
        plusButton.setDisable(true);
        minusButton.setDisable(true);
      }
      plusButton.setOnAction(createTakeTokenHandler(displayLabel));
      minusButton.setOnAction(createBackTokenHandler(displayLabel));
    }
    Button confirmButton = getConfirmButton();
    if (returnTokenActionMap.size() != 0) {
      confirmButton.setText("Return");
    } else {
      confirmButton.setText("Confirm");
    }
    confirmButton.setDisable(true);
  }


  /**
   * Set up the TokenBank GUI object.
   *
   * @param bankMap    the enum map of price
   * @param layoutX    layout x
   * @param layoutY    layout y
   * @param firstSetup whether it's first set up or not
   */
  public void setup(Map<String, TakeTokenAction> takeTokenActionMap,
                    EnumMap<Colour, Integer> bankMap,
                    double layoutX, double layoutY, boolean firstSetup) {
    this.takeTokenActionMap = takeTokenActionMap;
    // set the layout of the GUI
    if (firstSetup) {
      setLayoutX(layoutX);
      setLayoutY(layoutY);
      bindActionToButtonAndLabel();
    }
    setColourTokenBankMap(bankMap);
  }



  /**
   * Set up the TokenBank GUI object.
   *
   * @param bankMap    the enum map of price
   * @param layoutX    layout x
   * @param layoutY    layout y
   * @param firstSetup whether it's first set up or not
   */
  public void setupReturnToken(Map<String, ReturnTokenAction> returnTokenActionMap,
                               EnumMap<Colour, Integer> bankMap,
                               double layoutX, double layoutY, boolean firstSetup) {
    this.returnTokenActionMap = returnTokenActionMap;

    //TODO DEBUG -- DELETE LATER
    for (String actionId : returnTokenActionMap.keySet()) {
      ReturnTokenAction action = returnTokenActionMap.get(actionId);
      System.out.println("Return Action ID: " + actionId
          + ", tokens to return: " + action.getExtraTokenCount());

    }
    // set the layout of the GUI
    setLayoutX(layoutX);
    setLayoutY(layoutY);
    bindActionToButtonAndLabel();
    setColourTokenBankMap(bankMap);

  }
}

