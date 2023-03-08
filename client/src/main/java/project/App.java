package project;

import ca.mcgill.comp361.splendormodel.model.Colour;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.connection.GameRequestSender;
import project.connection.LobbyRequestSender;
import project.controllers.stagecontrollers.GameController;
import project.controllers.stagecontrollers.LobbyController;
import project.view.lobby.SessionGuiManager;
import project.view.lobby.communication.User;


/**
 * Splendor Game App.
 */
public class App extends Application {


  // One and the only one requestSender
  private static final LobbyRequestSender lobbyRequestSender =
      new LobbyRequestSender("http://76.66.139.161:4242");
  // TODO: Change this to singleton later LobbyServiceRequestSender
  //private static final LobbyRequestSender lobbyRequestSender =
  //    new LobbyRequestSender("http://127.0.0.1:4242");
  /**/
  //http://127.0.0.1:4246/splendor
  //http://76.66.139.161:4246/splendorbvb
  private static final GameRequestSender gameRequestSender =
      new GameRequestSender("http://76.66.139.161:4246/", "splendorbase");
  private static final Colour[] allColours = new Colour[] {
      Colour.RED, Colour.BLACK, Colour.WHITE, Colour.BLUE, Colour.GREEN, Colour.GOLD
  };
  // TODO: This should not be a global variable in App!!!
  //private static final GameRequestSender gameRequestSender =
  //    new GameRequestSender(
  //        "http://127.0.0.1:4246/", "splendorbase");
  private static final Colour[] baseColours = new Colour[] {
      Colour.RED, Colour.BLACK, Colour.WHITE, Colour.BLUE, Colour.GREEN
  };
  private final static List<Thread> gameGuiThread = new ArrayList<>();
  private static final Map<Long, GameController> gameControllerMap = new HashMap<>();
  private static Stage primaryStage;
  private static User user;
  private static GameBoardLayoutConfig guiLayouts;
  private static Thread lobbyGuiThread = null;
  private static LobbyController lobbyController = null;

  public static void main(String[] args) {
    launch();
  }

  public static LobbyRequestSender getLobbyServiceRequestSender() {
    return lobbyRequestSender;
  }

  public static GameRequestSender getGameRequestSender() {
    return gameRequestSender;
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

  public static void setUser(User puser) {
    user = puser;
  }

  public static GameBoardLayoutConfig getGuiLayouts() {
    return guiLayouts;
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

  /**
   * Show a popup Stage with the corresponding fxml file, controller class, and the width/height.
   *
   * @param fxmlName         fxml file name
   * @param controller       controller class of the popup
   * @param popUpStageWidth  window width
   * @param popUpStageHeight window height
   * @throws IOException in case fxml is not found
   */
  public static void loadPopUpWithController(String fxmlName, Object controller, Rectangle cover,
                                             double popUpStageWidth, double popUpStageHeight)
      throws IOException {

    // the new stage is shown, make the rectangle visible
    //cover.setVisible(true);
    //cover.setOpacity(0.2);
    //cover.toFront();
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlName));
    fxmlLoader.setController(controller);
    Stage newStage = new Stage();
    newStage.setScene(new Scene(fxmlLoader.load(), popUpStageWidth, popUpStageHeight));
    newStage.getIcons().add(new Image("project/pictures/back/splendor-icon.jpg"));
    // make the new popup window always stay on top level
    newStage.setAlwaysOnTop(true);
    // establish a relationship between two window (popup and primary)
    newStage.initOwner(primaryStage);
    // block user from clicking on the main stage
    newStage.initModality(Modality.WINDOW_MODAL);
    // disable the full screen (green one) button for mac
    newStage.initStyle(StageStyle.UTILITY);
    //newStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    // show the popup window
    newStage.show();
    //newStage.setOnCloseRequest(closeEvent -> {
    //  cover.setVisible(false);
    //});
  }

  /**
   * Show a popup Stage with the corresponding fxml file, controller class, and the width/height.
   *
   * @param fxmlName   fxml file name
   * @param controller controller class of the popup
   * @throws IOException in case fxml is not found
   */
  public static void loadNewSceneToPrimaryStage(String fxmlName, Object controller)
      throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlName));
    fxmlLoader.setController(controller);
    double width = primaryStage.getScene().getWidth();
    double height = primaryStage.getScene().getHeight();
    // setting the scene might turn off full screen mode, must reset to full again immediately
    primaryStage.setScene(new Scene(fxmlLoader.load(), width, height));
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

  public static void addGameThread(Thread thread) {
    gameGuiThread.add(thread);
  }

  public static void killGameThread() {
    for (Thread thread : gameGuiThread) {
      thread.interrupt();
    }
    gameGuiThread.clear();
  }

  public static Thread getAppLobbyGuiThread() {
    return App.lobbyGuiThread;
  }

  public static void setAppLobbyGuiThread(Thread thread) {
    App.lobbyGuiThread = thread;
  }

  public static List<Thread> getGameGuiThreads() {
    return gameGuiThread;
  }

  public static LobbyController getLobbyController() {
    return lobbyController;
  }

  public static void setLobbyController(LobbyController lobbyController) {
    App.lobbyController = lobbyController;
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
    try {
      FileReader f = new FileReader(Objects.requireNonNull(
          App.class.getClassLoader().getResource("appConfig.json")).getFile());
      JsonReader jfReader = new JsonReader(f);
      guiLayouts = new Gson().fromJson(jfReader, GameBoardLayoutConfig.class);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    FXMLLoader startPageLoader = new FXMLLoader(App.class.getResource("start_page.fxml"));
    SessionGuiManager.getInstance();
    Scene scene = new Scene(startPageLoader.load(),
        guiLayouts.getAppWidth(),
        guiLayouts.getAppHeight());
    primaryStage.setTitle("Welcome to Splendor!");
    primaryStage.getIcons().add(new Image("project/pictures/back/splendor-icon.jpg"));
    primaryStage.setFullScreenExitHint("");
    primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    primaryStage.setFullScreen(true);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

}