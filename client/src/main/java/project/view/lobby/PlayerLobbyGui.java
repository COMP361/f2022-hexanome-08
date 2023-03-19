package project.view.lobby;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import project.controllers.guielementcontroller.PlayerLobbyGuiController;
import project.view.lobby.communication.Player;

public class PlayerLobbyGui extends HBox {
  private final Player player;

  public PlayerLobbyGui(Player player) {
    this.player = player;
    FXMLLoader fxmlLoader = new FXMLLoader(getClass()
        .getResource("/project/player_lobby_gui.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(new PlayerLobbyGuiController(player));
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Player getPlayer() {
    return player;
  }
}
