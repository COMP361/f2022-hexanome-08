package project.view.lobby;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import project.controllers.guielementcontroller.PlayerLobbyGuiController;
import project.view.lobby.communication.Player;

public class PlayerLobbyGui extends HBox {
  private final Player player;
  private final PlayerLobbyGuiController controller;

  public PlayerLobbyGui(Player player) {
    this.player = player;
    FXMLLoader fxmlLoader = new FXMLLoader(getClass()
        .getResource("/project/player_lobby_gui.fxml"));
    fxmlLoader.setRoot(this);
    controller = new PlayerLobbyGuiController(player);
    fxmlLoader.setController(controller);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Player getPlayer() {
    return player;
  }

  public void setBorderColour() {
    String colourString = controller.getColourStringFromColourPicker();
    this.setStyle("-fx-border-width:5; -fx-border-color:#" + colourString);
  }
}
