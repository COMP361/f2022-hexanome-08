package project.view.splendor;
import java.io.IOException;
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

public class TokenBankGui extends HBox implements NumOfPlayerDependentGui{

  public TokenBankGui() {
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
    VBox confirmVbox = (VBox) allChildren.get(allChildren.size()-1);
    return (Button) confirmVbox.getChildren().get(1);
  }
  private EventHandler<ActionEvent> createTakeTokenHandler(
      Label displayZone, Colour curColour) {
    return event -> {
      // add only if it's a valid take
      // for this colour
      // and for all colour
      int curNum = Integer.parseInt(displayZone.getText());
      int tokenLeft = Integer.parseInt(this.getColourTokenBankMap().get(curColour).getText());
      // can only increment if 0 <= takeNum < 2 && bank > 0

      int uniqueColourTakenCount = 0;
      for (Colour c: this.getTakeTokenDecision().keySet()) {
        int curColourTake = this.getTakeTokenDecision().get(c);
        if (curColourTake > 0) {
          uniqueColourTakenCount += 1;
        }
      }

      if (curNum == 0 && uniqueColourTakenCount == 2) {
        curNum += 1;
        String newNum = curNum + "";
        displayZone.setText(newNum);
      } else {
        if (!(curNum == 1 && uniqueColourTakenCount == 2)
        && curNum < 2 && curNum >= 0 && tokenLeft > 0
        && !banFromTaking(uniqueColourTakenCount)){
          // then we can have this Label being changed
          if(curNum == 1) {
            if (tokenLeft >= 4) {
              curNum += 1;
              String newNum = curNum + "";
              displayZone.setText(newNum);
            }
          } else {
            curNum += 1;
            String newNum = curNum + "";
            displayZone.setText(newNum);
          }

        }
      }
      uniqueColourTakenCount = 0;
      for (Colour c: this.getTakeTokenDecision().keySet()) {
        int curColourTake = this.getTakeTokenDecision().get(c);
        if (curColourTake > 0) {
          uniqueColourTakenCount += 1;
        }
      }
      if (banFromTaking(uniqueColourTakenCount)) {
        getConfirmButton().setDisable(false);
      }
    };
  }

  private EventHandler<ActionEvent> createBackTokenHandler(
      Label displayZone) {
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

  private EventHandler<ActionEvent> createConfirmTakeTokenHandler() {
    // TODO: Need to send the request to Game server for future use
    return event -> {
      Map<Colour, Text> bankTokenBankMap = getColourTokenBankMap();
      Map<Colour, Integer> playerDecision = getTakeTokenDecision();
      Map<Colour, Label> playerNewDecisions = getColourTokenTakeMap();
      for (Colour c : playerDecision.keySet()) {
        if (!c.equals(Colour.GOLD)) {
          Text bankBalanceText = bankTokenBankMap.get(c);
          Label playerNewChoice = playerNewDecisions.get(c);
          int curTokenLeft = Integer.parseInt(bankBalanceText.getText());
          int curTokenTake = playerDecision.get(c);
          int newBankBalance = curTokenLeft - curTokenTake;
          playerNewChoice.setText("0");
          bankBalanceText.setText(newBankBalance+"");
        }
      }
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
    for (Colour c: this.getTakeTokenDecision().keySet()) {
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

  private Map<Colour, Label> getColourTokenTakeMap() {
    // processing token number Text of 5 regular token
    Map<Colour,Label> resultMap = new HashMap<>();
    ObservableList<Node> allChildren = this.getChildren();
    Colour[] colours = App.getBaseColours();
    for (int i = 0; i < 5; i++) {
      Group curGroup = (Group) allChildren.get(i);
      Label takeTokenNum = (Label) curGroup.getChildren().get(curGroup.getChildren().size()-2);
      resultMap.put(colours[i],takeTokenNum);
    }
    return resultMap;
  }

  /**
   * Return the bank balance of each token left in bank (including gold).
   *
   * @return a map from colour to the GUI object Text containing the bank balance
   */
  public Map<Colour,Text> getColourTokenBankMap() {
    Map<Colour,Text> resultTextList = new HashMap<>();
    ObservableList<Node> allChildren = this.getChildren();
    Colour[] colours = App.getAllColours();
    // processing token number Text of 5 regular token
    for (int i = 0; i < 5; i++) {
      Group curGroup = (Group) allChildren.get(i);
      Text curTokenNum = (Text) curGroup.getChildren().get(curGroup.getChildren().size()-1);
      resultTextList.put(colours[i],curTokenNum);
    }

    // processing the gold token number Text
    VBox goldToken = (VBox) allChildren.get(5);
    Group goldTokenGroup = (Group) goldToken.getChildren().get(1);
    Text curTokenNum = (Text) goldTokenGroup.getChildren().get(1);
    resultTextList.put(colours[5], curTokenNum);
    return resultTextList;
  }

  private void setColourTokenBankMap(Map<Colour,Integer> bankBalanceMap) {
    Map<Colour, Text> curBankTokenBankMap = getColourTokenBankMap();
    Colour[] allColours = App.getAllColours();
    for(Colour c : allColours) {
      Text curText = curBankTokenBankMap.get(c);
      int newBalance = bankBalanceMap.get(c);
      curText.setText(newBalance+"");
    }
  }

  /**
   * Get the take token decision of user.
   *
   * @return a map from colour to the GUI object Text containing the user decision
   */
  private Map<Colour, Integer> getTakeTokenDecision() {
    Map<Colour, Integer> result = new HashMap<>();
    for (Colour c : this.getColourTokenTakeMap().keySet()) {
      int curTake = Integer.parseInt(this.getColourTokenTakeMap().get(c).getText());
      result.put(c,curTake);
    }
    return result;
  }

  /**
   * Bind all functionality to the GUI objects we care.
   *
   */
  private void bindButtonAndLabel() {
    ObservableList<Node> allChildren = this.getChildren();
    Colour[] colours = App.getBaseColours();
    // iterate through all five regular tokens
    for (int i = 0; i < 5; i++) {
      Group curGroup = (Group) allChildren.get(i);
      Colour curColour = colours[i];
      Button plusButton = (Button) curGroup.getChildren().get(1);
      Button minusButton = (Button) curGroup.getChildren().get(2);
      Label displayLabel = (Label) curGroup.getChildren().get(curGroup.getChildren().size()-2);
      plusButton.setOnAction(createTakeTokenHandler(displayLabel, curColour));
      minusButton.setOnAction(createBackTokenHandler(displayLabel));
    }
    Button confirmButton = getConfirmButton();
    confirmButton.setDisable(true);
    confirmButton.setOnAction(createConfirmTakeTokenHandler());
  }


  public void setup(int numOfPlayer, double layoutX, double layoutY) {
    // set the layout of the GUI
    setLayoutX(layoutX);
    setLayoutY(layoutY);

    // GUI class dependent things to setup
    int baseTokenCount = 0;
    if (numOfPlayer == 4) {
      baseTokenCount = 7;
    } else if (numOfPlayer == 3) {
      baseTokenCount = 5;
    } else if (numOfPlayer == 2){
      baseTokenCount = 4;
    }
    Colour[] colours =  App.getAllColours();
    Map<Colour, Integer> bankMap = new HashMap<>();
    for (Colour c : colours) {
      if(c.equals(Colour.GOLD)) {
        bankMap.put(c, 5);
      } else {
        bankMap.put(c,baseTokenCount);
      }
    }
    setColourTokenBankMap(bankMap);
    bindButtonAndLabel();
  }
}
