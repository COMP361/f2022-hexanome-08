package project.controllers.stagecontrollers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;
import org.json.JSONObject;
import project.App;
import project.config.ConnectionConfig;
import project.connection.LobbyRequestSender;
import project.controllers.popupcontrollers.lobbypopup.AppSettingPageController;
import project.view.lobby.communication.User;

/**
 * Lobby GUI controller.
 */
public class LogInController implements Initializable {

  @FXML
  private TextField userName;

  @FXML
  private PasswordField userPassword;

  @FXML
  private Label logInPageErrorMessage;

  @FXML
  private Button logInButton;

  @FXML
  private Button quitButton;

  @FXML
  private Button settingButton;

  /**
   * Constructor of LogInController.
   */
  public LogInController() {}

  /**
   * The logic of handling log in. The methods check if the user has input both username and user
   * password or not
   */
  private void doLogIn() {
    // extract fields from the object, in case of failing to extract "access_token",
    // update the error message
    boolean serviceLogIn = false;
    try {
      String userNameStr = userName.getText();
      String userPasswordStr = userPassword.getText();
      // retrieve the parsed JSONObject from the response
      LobbyRequestSender lobbyRequestSender = App.getLobbyServiceRequestSender();
      JSONObject logInResponseJson = lobbyRequestSender
          .sendLogInRequest(userNameStr, userPasswordStr);
      // set up the permanent refresh_token for user
      String accessToken = logInResponseJson.getString("access_token");
      String refreshToken = logInResponseJson.getString("refresh_token");
      String authority = lobbyRequestSender.sendAuthorityRequest(accessToken);
      User curUser = new User(userNameStr, accessToken, refreshToken, authority);
      // bind the user to the scope of App running lifecycle
      App.setUser(curUser);
      if (authority.equals("ROLE_SERVICE")) {
        serviceLogIn = true;
        throw new RuntimeException("");
      }

      // at the same time, spawn a thread that keeps refreshing this player's access token
      Thread refreshTokenThread = createRefreshTokenThread();
      refreshTokenThread.setDaemon(true);
      refreshTokenThread.start();
      LobbyController lobbyController = new LobbyController();
      lobbyController.initializeRefreshTokenThread(refreshTokenThread);
      // display the lobby page, the role of the user will be used inside to
      // decide whether to display admin zone button or not
      App.loadNewSceneToPrimaryStage("lobby_page.fxml", lobbyController);

    } catch (Exception e) {
      if (!serviceLogIn) {
        logInPageErrorMessage.setText("Please enter both valid username and password");
      } else {
        logInPageErrorMessage.setText("Service Role can not log in LS! Try again");
      }

      userName.setText("");
      userPassword.setText("");
    }
  }

  // Mainly for debug usage
  private void setDefaultLogInInfo() {
    ConnectionConfig config = App.getConnectionConfig();
    if (config.isUseDefaultUserInfo()) {
      userName.setText(config.getDefaultUserName());
      userPassword.setText(config.getDefaultPassword());
    }
  }

  private Thread createRefreshTokenThread() {
    return new Thread(() -> {
      System.out.println(Thread.currentThread().getName() + ", the refresh token thread starts!");
      System.out.println("Refreshing for " + App.getUser().getUsername());
      while (!Thread.currentThread().isInterrupted()) {
        // refresh the token, then go to sleep for 450 seconds
        App.refreshUserToken(App.getUser());
        System.out.println(App.getUser().getAccessToken());
        try {
          Thread.sleep(450000);
        } catch (InterruptedException e) {
          System.out.println(Thread.currentThread().getName() + " is dead!");
          break;
        }
      }
    });
  }

  static class InvalidLogInCondition extends BooleanBinding {
    private final ObservableValue<String> userName;
    private final ObservableValue<String> password;

    public InvalidLogInCondition(ObservableValue<String> userName,
                                 ObservableValue<String> password) {
      this.userName = userName;
      this.password = password;
      bind(userName, password);
    }

    @Override
    protected boolean computeValue() {
      String userName = this.userName.getValue();
      String password = this.password.getValue();
      return (userName != null && userName.trim().isEmpty())
          || (password != null && password.trim().isEmpty());
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    setDefaultLogInInfo();

    ObservableValue<Boolean> invalidLoginInfoCondition = new InvalidLogInCondition(
        userName.textProperty(),
        userPassword.textProperty());

    logInButton.disableProperty().bind(invalidLoginInfoCondition);
    // enable log-in by pressing ENTER
    logInButton.setDefaultButton(true);
    logInButton.setOnAction(event -> {
      doLogIn();
    });


    // give setting button function to make a pop-up (display connection config info)
    settingButton.setOnAction(event -> {
      AppSettingPageController controller = new AppSettingPageController(App.getConnectionConfig());
      App.loadPopUpWithController("app_setting_page.fxml", controller,
          App.getGuiLayouts().getLargePopUpWidth(),
          App.getGuiLayouts().getLargePopUpHeight(),
          StageStyle.UTILITY);
    });

    // guarantee to execute the termination of program in javafx thread
    quitButton.setOnAction(event -> {
      // before quiting, also terminate the refresh token thread
      //Platform.exit();
      System.exit(0);
    });
  }
}
