package project.controllers.guielementcontroller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import project.view.lobby.communication.Session;

/**
 * SessionGuiController.
 */
public class SessionGuiController implements Initializable {

  private final Session session;
  @FXML
  private Label gameNameLabel;
  @FXML
  private Label creatorNameLabel;
  @FXML
  private Label playerInfosLabel;
  @FXML
  private Label saveGameIdLabel;
  @FXML
  private Button topButton;
  @FXML
  private Button btmButton;

  public SessionGuiController(Session session) {
    this.session = session;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    gameNameLabel.setText(session.getGameParameters().getDisplayName());

    String creatorInfo = String.format("creator: %s", session.getCreator());
    creatorNameLabel.setText(creatorInfo);

    String playerInfos = String.format("players [%s/%s-%s]: %s",
        session.getPlayers().size(),
        session.getGameParameters().getMinSessionPlayers(),
        session.getGameParameters().getMaxSessionPlayers(),
        session.getPlayers());
    playerInfosLabel.setText(playerInfos);

    if (!session.getSavegameid().isEmpty()) {
      saveGameIdLabel.setText("saved game id: " + session.getSavegameid());
    } else {
      saveGameIdLabel.setText("");
    }
  }
}
