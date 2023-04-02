package project.view.splendor.playergui;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import project.controllers.guielementcontroller.PlayerImageGuiController;

public class PlayerImageGui extends Group {

  private final PlayerImageGuiController controller;


  public PlayerImageGui(String playerName, int armCode) {
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/project/player_image.fxml"));
    fxmlLoader.setRoot(this);
    controller = new PlayerImageGuiController(playerName, armCode);
    fxmlLoader.setController(controller);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  public void setCurrentPointsText(int newPoints) {
    controller.getCurrentPointsText().setText("" + newPoints);
  }

  public void setReservedNobleCountText(int reservedNobleCount) {
    controller.getReservedNobleCountText().setText("" + reservedNobleCount);
  }

  public void setReservedCardsCountText(int reservedCardsCount) {
    controller.getReservedCardsCountText().setText("" + reservedCardsCount);
  }

  public void setHighlightRectangle(boolean highlight) {
    Rectangle highlightRectangle = controller.getHighlightRectangle();
    if (highlight) {
      highlightRectangle.setFill(Color.GREEN);
    } else {
      highlightRectangle.setFill(Color.WHITE);
    }
  }

}
