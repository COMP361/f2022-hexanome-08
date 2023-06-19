package project.controllers.stagecontrollers;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import project.App;
import project.controllers.popupcontrollers.UploadImagePopUpController;
import project.view.lobby.communication.Player;
import project.view.lobby.communication.User;

/**
 * AbstractLobbyController.
 */
public abstract class AbstractLobbyController implements Initializable {
  @FXML
  private ImageView userImageView;

  @FXML
  private VBox playerVisualInfoVbox;

  @FXML
  private Label userNameLabel;
  @FXML
  private Button logOutButton;

  private Thread sessionUpdateThread = null;

  private Thread refreshTokenThread = null;

  /**
   * Constructor of AbstractLobbyController.
   */
  public AbstractLobbyController() {}

  /**
   * initializeSessionUpdateThread.
   *
   * @param sessionUpdateThread sessionUpdateThread
   */
  protected void initializeSessionUpdateThread(Thread sessionUpdateThread) {
    if (this.sessionUpdateThread == null) {
      this.sessionUpdateThread = sessionUpdateThread;
    }
  }

  /**
   * initializeRefreshTokenThread.
   *
   * @param refreshTokenThread refreshTokenThread
   */
  protected void initializeRefreshTokenThread(Thread refreshTokenThread) {
    if (this.refreshTokenThread == null) {
      this.refreshTokenThread = refreshTokenThread;
    }
  }

  /**
   * getSessionUpdateThread.
   *
   * @return thread
   */
  public Thread getSessionUpdateThread() {
    return sessionUpdateThread;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // regular display set up for all users (admin or player)
    userImageView.setOnMouseClicked(event -> {
      App.loadPopUpWithController(
          "upload_image_page.fxml",
          new UploadImagePopUpController(),
          App.getGuiLayouts().getLargePopUpWidth(),
          App.getGuiLayouts().getLargePopUpHeight()
      );

    });

    String content = "Click to upload new picture!";
    App.bindToolTip(content, 15, userImageView, 20);

    userImageView.setImage(App.getPlayerImage(App.getUser().getUsername()));
    userNameLabel.setText("Current user: " + App.getUser().getUsername());

    Player player = App.getLobbyServiceRequestSender().getOnePlayer(
        App.getUser().getAccessToken(),
        App.getUser().getUsername());
    String playerPreferColor = player.getPreferredColour();
    System.out.println(playerPreferColor);
    playerVisualInfoVbox.setStyle("-fx-border-width:5; -fx-border-color:#" + playerPreferColor);

    logOutButton.setOnAction(event -> {
      // before leaving the lobby page, make sure to stop the update thread
      if (sessionUpdateThread != null) {
        sessionUpdateThread.interrupt();
        sessionUpdateThread = null;
      }
      // stop the refresh token thread as well
      if (refreshTokenThread != null) {
        refreshTokenThread.interrupt();
        refreshTokenThread = null;
      }

      // Reset the App user to null
      App.setUser(null);

      // jump back to start page
      App.loadNewSceneToPrimaryStage("start_page.fxml", new LogInController());
    });
  }
}
