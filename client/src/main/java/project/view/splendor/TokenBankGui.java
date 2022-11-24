package project.view.splendor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TokenBankGui extends HBox {

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

  public Map<Colour,Text> getColourTokenNumMap() {
    Map<Colour,Text> resultTextList = new HashMap<>();
    ObservableList<Node> allChildren = this.getChildren();
    Colour[] colours =  new Colour[] {
        Colour.RED, Colour.BLACK, Colour.WHITE, Colour.BLUE, Colour.GREEN, Colour.GOLD
    };
    // processing token number Text
    for (int i = 0; i < 5; i++) {
      Group curGroup = (Group) allChildren.get(i);
      Text curTokenNum = (Text) curGroup.getChildren().get(4);
      resultTextList.put(colours[i],curTokenNum);
    }
    VBox goldToken = (VBox) allChildren.get(5);
    Group goldTokenGroup = (Group) goldToken.getChildren().get(1);
    Text curTokenNum = (Text) goldTokenGroup.getChildren().get(1);
    resultTextList.put(colours[5], curTokenNum);
    return resultTextList;
  }

}
