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
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
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
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.config.ConnectionConfig;
import project.config.GameBoardLayoutConfig;
import project.connection.GameRequestSender;
import project.connection.LobbyRequestSender;
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
    return String.format("project/pictures/token/%s_tokens.png", colour.toString().toLowerCase());
  }

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
    String userPicPath = "project/pictures/user_pictures/";
    String randomPicPath = "project/pictures/random_pictures/";
    List<String> userPicNames = getPictureNames(userPicPath);
    List<String> randomPicNames = getPictureNames(randomPicPath);
    if (userPicNames.contains(playerName)) {
      return new Image(userPicPath + playerName + ".png");
    } else {
      // randomly pick one out of random list
      if (randomPicNames.size() > 0) {
        int randomIndex = new Random().nextInt(randomPicNames.size());
        String randomPicName = randomPicNames.get(randomIndex);
        return new Image(randomPicPath + randomPicName + ".png");
      } else {
        // ran out of pics!
        throw new RuntimeException("No random pictures available for you!");
      }
    }
  }

  private static List<String> getPictureNames(String resourcePath) {
    try {
      URI uri = App.class
          .getClassLoader()
          .getResource(resourcePath)
          .toURI();
      Path path = Paths.get(uri);

      try (Stream<Path> resourceStream = Files.list(path)) {
        return resourceStream
            .filter(Files::isRegularFile)
            .filter(Files::isRegularFile)
            .map(Path::getFileName)
            .map(Path::toString)
            .map(fileName -> fileName.substring(0, fileName.lastIndexOf('.')))
            .collect(Collectors.toList());
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e.getMessage());
      }

    } catch (URISyntaxException | NullPointerException e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }

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
          config.getSmallPopUpHeight());
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
          config.getSmallPopUpHeight());
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
          // Reset the App user to null
          App.setUser(null);
          // jump back to start page
          App.loadNewSceneToPrimaryStage("start_page.fxml", new LogInController());
        } else {
          App.loadNewSceneToPrimaryStage("admin_zone.fxml", new AdminPageController());
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
          config.getSmallPopUpHeight());
    });

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

}