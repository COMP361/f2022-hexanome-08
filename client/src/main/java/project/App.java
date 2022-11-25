package project;

import java.io.IOException;
import java.util.Map;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import project.connection.LobbyServiceRequestSender;
import project.view.lobby.Session;
import project.view.lobby.User;
import project.view.splendor.Colour;


/**
 * Splendor Game App.
 */
public class App extends Application {

  // The default scene used to display the initial window
  private static Scene scene;
  private static Scene handCard;

  private static Scene reservedCards;

  private static Scene lobby;

  // One and the only one requestSender
  private static final LobbyServiceRequestSender lobbyRequestSender =
      new LobbyServiceRequestSender("http://76.66.139.161:4242");

  private static final Colour[] allColours = new Colour[] {
    Colour.RED, Colour.BLACK, Colour.WHITE, Colour.BLUE, Colour.GREEN, Colour.GOLD
  };
  private static final Colour[] baseColours = new Colour[] {
      Colour.RED, Colour.BLACK, Colour.WHITE, Colour.BLUE, Colour.GREEN
  };

  private static User user;

  /**
   * Override the start() method to launch the whole project.
   *
   * @param stage The default stage to display
   * @throws IOException when fxml not found
   */
  @Override
  public void start(Stage stage) throws IOException {
    scene = new Scene(loadFxml("start_page"), 1200, 900);
    // Every time we loadFxml("a_file"), the file corresponding
    // controller's initialize method will get called
    //lobby = new Scene(loadFxml("admin_lobby_page"), 1000, 800);
    //handCard = new Scene(loadFxml("my_development_cards"), 789, 406);
    //reservedCards = new Scene(loadFxml("my_reserved_cards"), 789, 406);
    stage.setTitle("Welcome to Splendor!");
    stage.setScene(scene);
    stage.getIcons().add(new Image("project/pictures/back/splendor-icon.jpg"));
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }

  /**
   * Replace the current scene with the scene loaded from input fxml
   * file with the same layout ([640,400] by default).
   *
   * @param fxml The fxml file where we read the GUI setup
   * @throws IOException when fxml not found
   */
  static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  /**
   * Load a Scene from the fxml file to a new Stage with input height and width and title.
   *
   * @param fxml   The fxml file where we read the GUI setup
   * @param height Height of the new stage
   * @param width  Width of the new stage
   * @param title  Title of the new stage
   * @throws IOException when fxml not found
   */
  static void setRootWithSizeTitle(String fxml, int height, int width, String title)
      throws IOException {
    Stage newStage = new Stage();
    newStage.setTitle(title);
    newStage.setScene(new Scene(loadFxml(fxml), height, width));
    newStage.getIcons().add(new Image("project/pictures/back/splendor-icon.jpg"));
    newStage.show();
  }

  /**
   * Set the scene of the pop-up stage into a new scene loaded from fxml.
   *
   * @param fxml     The fxml file where we read the GUI setup
   * @param curScene The current scene of the pop-up
   * @throws IOException when fxml not found
   */
  static void setPopUpRoot(String fxml, Scene curScene) throws IOException {
    curScene.setRoot(loadFxml(fxml));
  }

  /**
   * Load a fxml file and return a Parent.
   *
   * @param fxml The fxml file where we read the GUI setup
   * @return A Parent that was loaded from the fxml file
   * @throws IOException when fxml not found
   */
  // Open another new stage as same size as the initial game stage
  private static Parent loadFxml(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  public static Scene getScene() {
    return scene;
  }

  public static void setHandCard() throws IOException {
    handCard.setRoot(loadFxml("my_development_cards"));
  }

  public static void setReserveCard() throws IOException {
    reservedCards.setRoot(loadFxml("my_reserved_cards"));
  }

  public static Scene getHandCard() {
    return handCard;
  }

  public static Scene getReservedCards() {
    return reservedCards;
  }



  public static LobbyServiceRequestSender getLobbyServiceRequestSender() {
    return lobbyRequestSender;
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

}