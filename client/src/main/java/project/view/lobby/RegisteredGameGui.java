package project.view.lobby;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import project.controllers.guielementcontroller.RegisteredGameGuiController;
import project.view.lobby.communication.GameParameters;

/**
 * RegisteredGameGui.
 */
public class RegisteredGameGui extends HBox {
  private final GameParameters gameParameters;

  /**
   *  RegisteredGameGui.
   *
   * @param gameParameters gameParameters
   */
  public RegisteredGameGui(GameParameters gameParameters) {
    this.gameParameters = gameParameters;
    FXMLLoader fxmlLoader = new FXMLLoader(getClass()
        .getResource("/project/game_service_gui.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(new RegisteredGameGuiController(gameParameters));
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }
}
