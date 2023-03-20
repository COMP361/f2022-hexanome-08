package project.controllers.stagecontrollers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import project.App;

public class AbstractLobbyController implements Initializable {
  @FXML
  protected ImageView userImageView;

  @FXML
  protected Label userNameLabel;

  @FXML
  protected Button logOutButton;

  protected Thread sessionUpdateThread;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // regular display set up for all users (admin or player)
    userImageView.setImage(App.getPlayerImage(App.getUser().getUsername()));
    userNameLabel.setText("Current user: " + App.getUser().getUsername());

    logOutButton.setOnAction(event -> {
      // before leaving the lobby page, make sure to stop the update thread
      //System.out.println("Interrupting lobby thread:" + sessionUpdateThread.getName());
      sessionUpdateThread.interrupt();
      //System.out.println(sessionUpdateThread.isAlive());
      // Reset the App user to null
      App.setUser(null);

      // jump back to start page
      App.loadNewSceneToPrimaryStage("start_page.fxml", new LogInController());
    });
  }
}
