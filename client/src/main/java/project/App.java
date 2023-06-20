package project;

import ca.mcgill.comp361.splendormodel.model.CityCard;
import ca.mcgill.comp361.splendormodel.model.Colour;
import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.config.ConnectionConfig;
import project.config.GameBoardLayoutConfig;
import project.connection.GameRequestSender;
import project.connection.LobbyRequestSender;
import project.controllers.popupcontrollers.AppSettingPageController;
import project.controllers.popupcontrollers.GameOverPopUpController;
import project.controllers.popupcontrollers.LobbyWarnPopUpController;
import project.controllers.stagecontrollers.AdminPageController;
import project.controllers.stagecontrollers.LogInController;
import project.controllers.stagecontrollers.SettingPageController;
import project.view.lobby.communication.Player;
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

  private static final EnumMap<Colour, String> colourStringMap = new EnumMap<>(Colour.class) {
    {
      put(Colour.RED, "#EE3A44");
      put(Colour.BLUE, "#1398FE");
      put(Colour.GREEN, "#00D074");
      put(Colour.GOLD, "#faff1f");
      put(Colour.BLACK, "#3C3D3C");
      put(Colour.WHITE, "WHITE");
    }
  };

  private static GameRequestSender gameRequestSender = null;
  private static LobbyRequestSender lobbyRequestSender = null;
  private static Stage primaryStage;
  private static User user;
  private static GameBoardLayoutConfig guiLayouts;

  private static ConnectionConfig connectionConfig;

  private static File connectionConfigFile;



  private static Stage currentPopupStage = null;

  /**
   * Constructor of javafx app.
   */
  public App() {}

  /**
   * main.
   *
   * @param args args
   */
  public static void main(String[] args) {
    // mute annoying logging msgs to console
    ch.qos.logback.classic.Logger rootLogger =
        (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    rootLogger.setLevel(ch.qos.logback.classic.Level.INFO);
    launch();
  }

  /**
   * get the lobby request sender.
   *
   * @return lobby request sender.
   */
  public static LobbyRequestSender getLobbyServiceRequestSender() {
    if (lobbyRequestSender == null) {
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

  // close the popup stage and reset the popup window buffer
  public static void closePopupStage(Stage stage) {
    stage.close();
    currentPopupStage = null;
  }

  /**
   * Show a popup Stage with the corresponding fxml file, controller class, and the width/height.
   *
   * @param fxmlName         fxml file name
   * @param controller       controller class of the popup
   * @param popUpStageWidth  window width
   * @param popUpStageHeight window height
   */
  public static void loadPopUpWithController(String fxmlName, Object controller,
                                             double popUpStageWidth, double popUpStageHeight,
                                             StageStyle stageStyle) {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlName));
    fxmlLoader.setController(controller);
    Stage newStage = new Stage();
    // reset the current pop up stage buffer
    newStage.setOnCloseRequest((WindowEvent event) -> {
      closePopupStage(newStage);
    });

    if (currentPopupStage == null) {
      currentPopupStage = newStage;
    }

    try {
      Scene popupScene = new Scene(fxmlLoader.load(), popUpStageWidth, popUpStageHeight);
      newStage.setScene(popupScene);
      // Adding the key event to the Scene
      popupScene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
          closePopupStage(newStage);
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    newStage.getIcons().add(new Image("project/pictures/back/splendor-icon.jpg"));
    // make the new popup window always stay on top level
    newStage.setAlwaysOnTop(true);
    // establish a relationship between two window (popup and primary)
    newStage.initOwner(primaryStage);

    // set stage style
    newStage.initStyle(stageStyle);

    // freeze the main stage when popup is shown
    newStage.initModality(Modality.WINDOW_MODAL);

    // show the popup window
    newStage.show();
  }

  /**
   * Close all popups of primary stage.
   */
  public static void closeAllPopUps() {
    for (Window window : Window.getWindows()) {
      if (window instanceof Stage) {
        Stage popup = (Stage) window;
        if (popup.getOwner() == primaryStage) {
          popup.close();
        }
      }
    }
  }

  /**
   * Show a popup Stage with the corresponding fxml file, controller class, and the width/height.
   *
   * @param fxmlName   fxml file name
   * @param controller controller class of the popup
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

  /**
   * Static getter for a list of all the colours in the Colour enum.
   *
   * @return a list of colours
   */
  public static Colour[] getAllColours() {
    return allColours;
  }

  /**
   * Static getter for a list of all the base colours in the Colour enum.
   *
   * @return list of only the base colours
   */
  public static Colour[] getBaseColours() {
    return baseColours;
  }

  /**
   * Static getter.
   *
   * @return user
   */
  public static User getUser() {
    return user;
  }

  /**
   * Sets the user passed in the parameter as the user.
   *
   * @param newUser new user to be set as user.
   */
  public static void setUser(User newUser) {
    user = newUser;
  }

  /**
   * Static getter.
   *
   * @return the game board layout configuration
   */
  public static GameBoardLayoutConfig getGuiLayouts() {
    return guiLayouts;
  }

  /**
   * Static getter.
   *
   * @return the connection configuration
   */
  public static ConnectionConfig getConnectionConfig() {
    return connectionConfig;
  }

  public static File getConnectionConfigFile() {
    return connectionConfigFile;
  }


  public static Stage getCurrentPopupStage() {
    return currentPopupStage;
  }

  /**
   * Static getter.
   *
   * @param cardName name of the noble whose path you want to get
   * @return the path to the picture of the noble
   */
  public static String getNoblePath(String cardName) {
    return String.format("project/pictures/noble/%s.png", cardName);
  }


  /**
   * Static getter.
   * Note that arm code can only be 1,2,3,4.
   *
   * @param armCode specific shield whose location you want to get
   * @return the path to the shield
   */
  public static String getArmPath(int armCode) {
    assert armCode >= 1 && armCode <= 4;
    return String.format("project/pictures/power/arm%s.png", armCode);
  }

  /**
   * Static getter.
   *
   * @param cardName name of the orient card whose path you want to get
   * @param level level of the orient card whose path you want to get
   * @return path of the orient card
   */
  public static String getOrientCardPath(String cardName, int level) {
    assert level >= 1 && level <= 3;
    return String.format("project/pictures/orient/%d/%s.png", level, cardName);
  }

  /**
   * Static getter.
   *
   * @param cardName name of the base card whose path you want to get
   * @param level level of the base card whose path you want to get
   * @return path of the base card
   */
  public static String getBaseCardPath(String cardName, int level) {
    assert level >= 1 && level <= 3;
    return String.format("project/pictures/level%d/%s.png", level, cardName);
  }

  /**
   * Static getter.
   *
   * @param colour colour of the token whose image you want to get
   * @return path of the image of the token
   */
  public static String getTokenPath(Colour colour) {
    return String.format("project/pictures/token/%s_tokens.png", colour.toString().toLowerCase());
  }

  /**
   * Static getter.
   *
   * @param cityCard city whose image you want to get
   * @return path to the image of the city
   */
  public static String getCityPath(CityCard cityCard) {
    return String.format("project/pictures/cities/%s.png", cityCard.getCardName());
  }

  /**
   * getPlayerImage.
   *
   * @param playerName playerName
   * @return Image
   */
  public static Image getPlayerImage(String playerName) {
    try {
      return getLobbyServiceRequestSender().getUserImage(getUser().getAccessToken(), playerName);
    } catch (UnirestException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Static getter.
   *
   * @return enum map from colour to string of the colour name
   */
  public static EnumMap<Colour, String> getColourStringMap() {
    return new EnumMap<>(colourStringMap);
  }

  /**
   * Bind a tooltip to display (on-hover text hint) to any javafx node.
   *
   * @param toolTipContent         content of the on-hover text
   * @param toolTipContentFontSize font size of the content
   * @param toolTipBindingElement  the javafx node that you want to bind this tooltip to
   * @param displayDelayMillis     display delay (how long you need to wait before seeing the text)
   */
  public static void bindToolTip(String toolTipContent, int toolTipContentFontSize,
                                 Node toolTipBindingElement, double displayDelayMillis) {
    Tooltip tooltip = new Tooltip(toolTipContent);
    tooltip.setShowDelay(Duration.millis(displayDelayMillis));
    tooltip.setStyle(String.format("-fx-font-size: %spx;", toolTipContentFontSize));
    toolTipBindingElement.setOnMouseEntered(e -> {
      Tooltip.install(toolTipBindingElement, tooltip);
    });

    toolTipBindingElement.setOnMouseExited(e -> {
      Tooltip.uninstall(toolTipBindingElement, tooltip);
    });
  }

  /**
   * colorToColourString.
   *
   * @param chosenColor chosenColor
   * @return string
   */
  public static String colorToColourString(Color chosenColor) {
    // Convert the color to a 16-byte encoded string
    return String.format("%02X%02X%02X%02X",
        (int) (chosenColor.getRed() * 255),
        (int) (chosenColor.getGreen() * 255),
        (int) (chosenColor.getBlue() * 255),
        (int) (chosenColor.getOpacity() * 255));
  }


  /**
   * bindColourUpdateAction.
   *
   * @param player player
   * @param colorPicker colorPicker
   * @param refreshSettingPage refreshSettingPage
   * @param colorUpdateButton colorUpdateButton
   * @param config config
   */
  public static void bindColourUpdateAction(Player player, ColorPicker colorPicker,
                                            boolean refreshSettingPage, Button colorUpdateButton,
                                            GameBoardLayoutConfig config) {
    Color color = Color.web(player.getPreferredColour());
    colorPicker.setValue(color);
    colorUpdateButton.setOnAction(event -> {
      // Convert the color to a 16-byte encoded string
      String msg;
      String title;
      String colorString = colorToColourString(colorPicker.getValue());
      try {
        App.getLobbyServiceRequestSender().updateOnePlayerColour(
            App.getUser().getAccessToken(),
            player.getName(),
            colorString
        );
        // depending on the flag, decide which page are we on to refresh
        if (refreshSettingPage) {
          App.loadNewSceneToPrimaryStage("setting_page.fxml", new SettingPageController());
        } else {
          App.loadNewSceneToPrimaryStage("admin_zone.fxml", new AdminPageController());
        }
        title = "Colour Update Confirmation";
        msg = "Updated correctly!";
      } catch (UnirestException e) {
        // somehow failed to update the colour
        e.printStackTrace();
        title = "Colour Selection Error";
        msg = "Could not update user's new colour choice!\nPlease try again";
      }

      App.loadPopUpWithController("lobby_warn.fxml",
          new LobbyWarnPopUpController(msg, title),
          config.getSmallPopUpWidth(),
          config.getSmallPopUpHeight(),
          StageStyle.UTILITY);
    });
  }

  /**
   * bindPasswordUpdateAction.
   *
   * @param player player
   * @param passwordField passwordField
   * @param passwordUpdateButton passwordUpdateButton
   * @param config config
   */
  public static void bindPasswordUpdateAction(Player player, PasswordField passwordField,
                                              Button passwordUpdateButton,
                                              GameBoardLayoutConfig config) {
    passwordUpdateButton.setOnAction(event -> {
      String msg;
      String title;
      try {
        App.getLobbyServiceRequestSender()
            .updateOnePlayerPassword(
                App.getUser().getAccessToken(),
                player.getName(),
                player.getPassword(),
                passwordField.getText());
        title = "Password Update Confirmation";
        msg = "Updated correctly!";

      } catch (UnirestException e) {
        title = "Password Update Error";
        msg = "Wrong password format.";
      }

      App.loadPopUpWithController("lobby_warn.fxml",
          new LobbyWarnPopUpController(msg, title),
          config.getSmallPopUpWidth(),
          config.getSmallPopUpHeight(),
          StageStyle.UTILITY);
      passwordField.clear();
    });
  }

  /**
   * bindDeleteUserAction.
   *
   * @param player player
   * @param deletePlayerButton deletePlayerButton
   * @param backToLogInPage backToLogInPage
   * @param config config
   */
  public static void bindDeleteUserAction(Player player, Button deletePlayerButton,
                                          boolean backToLogInPage, GameBoardLayoutConfig config) {
    deletePlayerButton.setOnAction(event -> {
      String msg;
      String title;
      try {
        App.getLobbyServiceRequestSender()
            .deleteOnePlayer(App.getUser().getAccessToken(), player.getName());
        // if we should go back to log in page (deleted at setting page)
        // then load the start page, otherwise refresh the admin zon
        if (backToLogInPage) {
          backToLogInPage();
        } else {
          loadNewSceneToPrimaryStage("admin_zone.fxml", new AdminPageController());
        }

        title = "Delete Player Confirmation";
        msg = "Deleted correctly!";

      } catch (UnirestException e) {
        title = "Delete Player Error";
        msg = "Player was unable to be deleted.";
      }

      App.loadPopUpWithController("lobby_warn.fxml",
          new LobbyWarnPopUpController(msg, title),
          config.getSmallPopUpWidth(),
          config.getSmallPopUpHeight(),
          StageStyle.UTILITY);
    });

  }


  /**
   * Reset user, and load the login page fxml.
   *
   */
  public static void backToLogInPage() {
    // Reset the App user to null
    App.setUser(null);
    // jump back to start page
    App.loadNewSceneToPrimaryStage("start_page.fxml", new LogInController());
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
    connectionConfigFile = new File("connectionConfig.json");


    try {
      String gameConfigString = FileUtils.readFileToString(gameConfigFile, StandardCharsets.UTF_8);
      String connectConfigJson
          = FileUtils.readFileToString(connectionConfigFile, StandardCharsets.UTF_8);
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

}