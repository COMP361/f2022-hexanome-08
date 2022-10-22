package group08;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PrimaryController {

    @FXML
    private Pane purchaseContent;

    @FXML
    private Button quitGameButton;

    @FXML
    private TextField userName;

    @FXML
    private PasswordField userPassword;

    @FXML
    private Label logInPageErrorMessage;

    @FXML
    protected void onLogInButtonClick() throws IOException {
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("splendor_game_board.fxml")));

        /*
        TODO: before set up and display the scene, should check logging condition correctness
         */

        String userNameStr = userName.getText();
        String userPasswordStr = userPassword.getText();


        // For the sake of simplicity, we only check if password and username exist
        if (Objects.equals(userNameStr, "") || Objects.equals(userPasswordStr, "")){
            logInPageErrorMessage.setText("Please enter a username and password");
        }

        else {
            App.setRoot("splendor_game_board");
        }

    }

    @FXML
    protected void onLogOutMenuItemClick() throws IOException{
        App.setRoot("splendor");
    }

    @FXML
    protected void onQuitGameButtonClick(){
        Stage window = (Stage) quitGameButton.getScene().getWindow();
        window.close();
    }

    @FXML
    protected void onDevelopmentCardShapeClick() throws IOException {
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("splendor_purchase.fxml")));
        App.setRootWithSizeTitle("splendor_purchase",360,170,"Make your decision");
//        Stage stage = new Stage();
//        stage.setTitle("Make your decision");
//        stage.setScene(new Scene(root, 360, 170));
//        stage.show();
    }


    @FXML
    protected void madePurchase(){
        Stage window = (Stage) purchaseContent.getScene().getWindow();

        /* TODO: Check condition if the purchase can be done successfully
            then close the window
         */
        window.close();
    }

    @FXML
    protected void cancelAction(){
        Stage window = (Stage) purchaseContent.getScene().getWindow();
        // Just close the window without doing anything
        window.close();

    }

    @FXML
    protected void madeReserve(){
        Stage window = (Stage) purchaseContent.getScene().getWindow();

        /* TODO: Check condition if the reserve can be done successfully
            then close the window
         */
        window.close();

    }
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
