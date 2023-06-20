package project.controllers.guielementcontroller;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.StageStyle;
import project.App;
import project.config.GameBoardLayoutConfig;
import project.connection.LobbyRequestSender;
import project.controllers.popupcontrollers.LobbyWarnPopUpController;
import project.controllers.stagecontrollers.AdminPageController;
import project.view.lobby.communication.GameParameters;

/**
 * RegisteredGameGuiController.
 */
public class RegisteredGameGuiController implements Initializable {
  private final GameParameters gameParameters;
  @FXML
  private Button unregisterButton;
  @FXML
  private Label serviceNameLabel;
  @FXML
  private Label displayNameLabel;

  /**
   * RegisteredGameGuiController.
   *
   * @param gameParameters gameParameters
   */
  public RegisteredGameGuiController(GameParameters gameParameters) {
    this.gameParameters = gameParameters;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    serviceNameLabel.setText("Service Name: " + gameParameters.getName());
    displayNameLabel.setText("Display Name: " + gameParameters.getDisplayName());
    LobbyRequestSender sender = App.getLobbyServiceRequestSender();
    GameBoardLayoutConfig config = App.getGuiLayouts();

    String warn = "THE UN-REGISTRATION CAN NOT BE REVERSED!!!";
    App.bindToolTip(warn, 15, unregisterButton, 20);

    unregisterButton.setOnAction(event -> {
      String msg;
      String title;
      try {
        sender.unregisterOneGameService(App.getUser().getAccessToken(), gameParameters.getName());
        //refresh the page
        App.loadNewSceneToPrimaryStage("admin_zone.fxml", new AdminPageController());
        title = "Game Service Unregister Confirmation";
        msg = "Game Service is removed from Lobby Service database!";
        App.loadPopUpWithController("lobby_warn.fxml",
            new LobbyWarnPopUpController(msg, title),
            config.getSmallPopUpWidth(),
            config.getSmallPopUpHeight(),
            StageStyle.UTILITY);
      } catch (UnirestException e) {
        title = "Delete Game Service Error";
        msg = "Game Service can not be removed from Lobby Service database!";
        App.loadPopUpWithController("lobby_warn.fxml",
            new LobbyWarnPopUpController(msg, title),
            config.getSmallPopUpWidth(),
            config.getSmallPopUpHeight(),
            StageStyle.UTILITY);
      }

    });
  }
}
