package project.controllers.stagecontrollers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import project.App;
import project.connection.LobbyRequestSender;
import project.view.lobby.PlayerLobbyGui;
import project.view.lobby.communication.Player;

public class AdminPageController implements Initializable {

  private List<Player> allRegisteredPlayers = new ArrayList<>();

  // app user related fields
  @FXML
  private ScrollPane allPlayersScrollPane;

  @FXML
  private VBox allPlayersVbox;

  @FXML
  private Button logOutButton;

  @FXML
  private Button settingButton;

  @FXML
  private Button lobbyPageButton;


  @FXML
  private ImageView userImageView;

  @FXML
  private Label userNameLabel;

  // new user related fields
  @FXML
  private ChoiceBox<String> rolesChoiceBox;

  @FXML
  private ColorPicker newUserColourPicker;


  @FXML
  private TextField userName;

  @FXML
  private PasswordField userPassword;

  @FXML
  private Button addUserButton;



  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    LobbyRequestSender sender = App.getLobbyServiceRequestSender();
    // set up the user gui in the scroll pane region
    String adminToken = App.getUser().getAccessToken();
    allRegisteredPlayers = Arrays.asList(sender.getPlayers(adminToken));
    Platform.runLater(() -> {
      for (Player player : allRegisteredPlayers) {
        allPlayersVbox.getChildren().add(new PlayerLobbyGui(player));
      }
      allPlayersScrollPane.setContent(allPlayersVbox);
    });

    // regular display set up for all users (admin or player)
    userImageView.setImage(App.getPlayerImage(App.getUser().getUsername()));
    userNameLabel.setText("Current user: " + App.getUser().getUsername());

    logOutButton.setOnAction(event -> {
      // Reset the App user to null
      App.setUser(null);

      // jump back to start page
      App.loadNewSceneToPrimaryStage("start_page.fxml", new LogInController());
    });

    // jump back to lobby page
    lobbyPageButton.setOnAction(event -> {
      Platform.runLater(() -> {
        App.loadNewSceneToPrimaryStage("lobby_page.fxml", new LobbyController());
      });
    });

    // TODO: Finish other features on admin zone


  }
}
