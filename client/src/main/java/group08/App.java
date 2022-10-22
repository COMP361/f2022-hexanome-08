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


    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("splendor"), 640, 400);
        stage.setTitle("Welcome to Splendor!");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static void setRootWithSizeTitle(String fxml, int height, int width, String title) throws IOException{
        Stage newStage = new Stage();
        newStage.setTitle(title);
        newStage.setScene(new Scene(loadFXML(fxml),height,width));
        newStage.show();
    }

    // Open another new stage as same size as the initial game stage
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}