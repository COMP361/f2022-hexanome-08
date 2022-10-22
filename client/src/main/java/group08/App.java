package group08;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    // The default scene used to display the initial window
    private static Scene scene;

    /**
     * Override the start() method to launch the whole project
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("splendor"), 640, 400);
        stage.setTitle("Welcome to Splendor!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Replace the current scene with the scene loaded from input fxml file with the same layout (640,400 by default)
     * @param fxml
     * @throws IOException
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Load a Scene from the fxml file to a new Stage with input height and width and title
     * @param fxml
     * @param height
     * @param width
     * @param title
     * @throws IOException
     */
    static void setRootWithSizeTitle(String fxml, int height, int width, String title) throws IOException{
        Stage newStage = new Stage();
        newStage.setTitle(title);
        newStage.setScene(new Scene(loadFXML(fxml),height,width));
        newStage.show();
    }

    /**
     * Load an fxml file and return a Parent
     * @param fxml
     * @return A Parent that was loaded from the fxml file
     * @throws IOException
     */
    // Open another new stage as same size as the initial game stage
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Program entry point
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

}