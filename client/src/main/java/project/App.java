package project;

import ca.mcgill.comp361.splendormodel.model.CityCard;
import ca.mcgill.comp361.splendormodel.model.Colour;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;
import project.config.ConnectionConfig;
import project.config.GameBoardLayoutConfig;
import project.connection.GameRequestSender;
import project.connection.LobbyRequestSender;
import project.controllers.popupcontrollers.GameOverPopUpController;
import project.controllers.stagecontrollers.LogInController;
import project.view.lobby.communication.User;


/**
 * Splendor Game App.
 */
public class App extends Application {
  private static final Colour[] allColours = new Colour[] {
      Colour.RED, Colour.BLACK, Colour.WHITE, Colour.BLUE, Colour.GREEN, Colour.GOLD
  };
  private static final Colour[] baseColours = new Colour[] {
      Colour.RED, Colour.BLACK, Colour.WHITE, Colour.BLUE, Colour.GREEN
  };
  private static final String defaultImageUrl =
      "https://cdn.pixabay.com/photo/2017/02/20/18/03/cat-2083492_960_720.jpg";
  private static GameRequestSender gameRequestSender = null;
  private static LobbyRequestSender lobbyRequestSender = null;
  private static Stage primaryStage;
  private static User user;
  private static GameBoardLayoutConfig guiLayouts;

  private static ConnectionConfig connectionConfig;

  public static void main(String[] args) {
    launch();
  }

  /**
   * Override the start() method to launch the whole project.
   *
   * @param stage The default stage to display
   * @throws IOException when fxml not found
   */
  @Override
  public void start(Stage stage) throws IOException {
    System.setProperty("com.apple.macos.useScreenMenuBar", "true");
    primaryStage = stage;
    File gameConfigFile = new File("appConfig.json");
    File connectConfigFile = new File("connectionConfig.json");


    try {
      String gameConfigString = FileUtils.readFileToString(gameConfigFile, StandardCharsets.UTF_8);
      String connectConfigJson
          = FileUtils.readFileToString(connectConfigFile, StandardCharsets.UTF_8);
      guiLayouts = new Gson().fromJson(gameConfigString, GameBoardLayoutConfig.class);
      connectionConfig = new Gson().fromJson(connectConfigJson, ConnectionConfig.class);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    FXMLLoader startPageLoader = new FXMLLoader(App.class.getResource("start_page.fxml"));
    // assign controller of log in page
    startPageLoader.setController(new LogInController());
    primaryStage.setTitle("Welcome to Splendor!");
    primaryStage.getIcons().add(new Image("project/pictures/back/splendor-icon.jpg"));
    primaryStage.setFullScreenExitHint("");
    primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    primaryStage.setFullScreen(true);
    Scene scene = new Scene(startPageLoader.load(),
        guiLayouts.getAppWidth(),
        guiLayouts.getAppHeight());
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * get the lobby request sender.
   *
   * @return lobby request sender.
   */
  public static LobbyRequestSender getLobbyServiceRequestSender() {
    if (lobbyRequestSender == null)  {
      String lobbyUrl = String.format("http://%s:4242", connectionConfig.getHostIp());
      lobbyRequestSender = new LobbyRequestSender(lobbyUrl);
    }
    return lobbyRequestSender;

  }

  /**
   * get the game request sender.
   *
   * @return game request sender.
   */
  public static GameRequestSender getGameRequestSender() {

    if (gameRequestSender == null) {
      String gameUrl = String.format("http://%s:4246/", connectionConfig.getHostIp());
      gameRequestSender = new GameRequestSender(gameUrl, "");
    }
    return gameRequestSender;
  }

  /**
   * Show a popup Stage with the corresponding fxml file, controller class, and the width/height.
   *
   * @param fxmlName         fxml file name
   * @param controller       controller class of the popup
   * @param popUpStageWidth  window width
   * @param popUpStageHeight window height
   * @throws IOException in case fxml is not found
   */
  public static void loadPopUpWithController(String fxmlName, Object controller,
                                             double popUpStageWidth, double popUpStageHeight) {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlName));
    fxmlLoader.setController(controller);
    Stage newStage = new Stage();
    try {
      newStage.setScene(new Scene(fxmlLoader.load(), popUpStageWidth, popUpStageHeight));
    } catch (IOException e) {
      e.printStackTrace();
    }
    newStage.getIcons().add(new Image("project/pictures/back/splendor-icon.jpg"));
    // make the new popup window always stay on top level
    newStage.setAlwaysOnTop(true);
    // establish a relationship between two window (popup and primary)
    newStage.initOwner(primaryStage);
    // block user from clicking on the main stage
    if (controller instanceof GameOverPopUpController) {
      // ban the close button on the game over pop up
      GameOverPopUpController gameOverPopUpController = (GameOverPopUpController) controller;
      if (!gameOverPopUpController.isOptionToCancel()) {
        newStage.initStyle(StageStyle.UNDECORATED);
      } else {
        newStage.initStyle(StageStyle.UTILITY);
      }
    } else {
      // disable the full screen (green one) button for mac
      newStage.initStyle(StageStyle.UTILITY);
    }
    newStage.initModality(Modality.WINDOW_MODAL);

    // show the popup window
    newStage.show();
  }

  /**
   * Show a popup Stage with the corresponding fxml file, controller class, and the width/height.
   *
   * @param fxmlName   fxml file name
   * @param controller controller class of the popup
   * @throws IOException in case fxml is not found
   */
  public static void loadNewSceneToPrimaryStage(String fxmlName, Object controller) {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlName));
    fxmlLoader.setController(controller);
    double width = primaryStage.getScene().getWidth();
    double height = primaryStage.getScene().getHeight();
    // setting the scene might turn off full screen mode, must reset to full again immediately
    try {
      primaryStage.setScene(new Scene(fxmlLoader.load(), width, height));
    } catch (IOException e) {
      e.printStackTrace();
    }
    primaryStage.setFullScreen(true);
  }

  /**
   * A static method to refresh the user's access token.
   *
   * @param user user
   */
  public static void refreshUserToken(User user) {
    String newAccessToken = lobbyRequestSender.sendRefreshTokenRequest(user.getRefreshToken());
    user.setAccessToken(newAccessToken);
  }

  public static Colour[] getAllColours() {
    return allColours;
  }

  public static Colour[] getBaseColours() {
    return baseColours;
  }

  public static User getUser() {
    return user;
  }

  public static void setUser(User newUser) {
    user = newUser;
  }

  public static GameBoardLayoutConfig getGuiLayouts() {
    return guiLayouts;
  }

  public static ConnectionConfig getConnectionConfig() {
    return connectionConfig;
  }


  public static String getNoblePath(String cardName) {
    return String.format("project/pictures/noble/%s.png", cardName);
  }

  // Note that arm code can only be 1,2,3,4
  public static String getArmPath(int armCode) {
    assert armCode >= 1 && armCode <= 4;
    return String.format("project/pictures/power/arm%s.png", armCode);
  }

  public static String getOrientCardPath(String cardName, int level) {
    assert level >= 1 && level <= 3;
    return String.format("project/pictures/orient/%d/%s.png", level, cardName);
  }

  public static String getBaseCardPath(String cardName, int level) {
    assert level >= 1 && level <= 3;
    return String.format("project/pictures/level%d/%s.png", level, cardName);
  }

  public static String getTokenPath(Colour colour) {
    return String.format("project/pictures/token/%s_tokens.png",colour.toString().toLowerCase());
  }

  public static String getCityPath(CityCard cityCard) {
    return String.format("project/pictures/cities/%s.png", cityCard.getCardName());
  }

  public static Image getPlayerImage(String playerName) {
    Image playerImage;
    try {
      playerImage = new Image(String.format("project/pictures/ta_pictures/%s.png", playerName));
    } catch (Exception e) {
      playerImage = new Image(defaultImageUrl);
    }
    return playerImage;
  }

}