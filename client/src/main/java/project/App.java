package project;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Splendor Game App.
 */
public class App extends Application {

  // The default scene used to display the initial window
  private static Scene scene;

  /**
   * Override the start() method to launch the whole project.
   *
   * @param stage The default stage to display
   * @throws IOException when fxml not found
   */
  @Override
  public void start(Stage stage) throws IOException {
    scene = new Scene(loadFxml("splendor"), 640, 400);
    stage.setTitle("Welcome to Splendor!");
    stage.setScene(scene);
    stage.show();
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
   * @param fxml The fxml file where we read the GUI setup
   * @param height Height of the new stage
   * @param width Width of the new stage
   * @param title Title of the new stage
   * @throws IOException when fxml not found
   */
  static void setRootWithSizeTitle(String fxml, int height, int width, String title)
      throws IOException {
    Stage newStage = new Stage();
    newStage.setTitle(title);
    newStage.setScene(new Scene(loadFxml(fxml), height, width));
    newStage.show();
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

  public static void main(String[] args) {
    launch();
  }

}