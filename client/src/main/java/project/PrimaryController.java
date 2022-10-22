package project;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The PrimaryController use to manage the general flow of the program.
 */
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

    /**
     * The logic of handling log in. The methods check if
     * the user has input both username and user password or not
     *
     * @throws IOException IOException if fxml file not found
     */
    @FXML
    protected void onLogInButtonClick() throws IOException {
        String userNameStr = userName.getText();
        String userPasswordStr = userPassword.getText();

        // For the sake of simplicity, we only check if password and username exist
        if (Objects.equals(userNameStr, "") || Objects.equals(userPasswordStr, "")) {
            logInPageErrorMessage.setText("Please enter a username and password");
        } else {
            App.setRoot("splendor_game_board");
        }

    }

    /**
     * Go back (reset the scene) to the Scene loaded from splendor.fxml
     *
     * @throws IOException IOException if fxml file not found
     */
    @FXML
    protected void onLogOutMenuItemClick() throws IOException {
        App.setRoot("splendor");
    }

    /**
     * Close the current stage once the quitGameButton has been clicked.
     */
    @FXML
    protected void onQuitGameButtonClick() {
        Stage curStage = (Stage) quitGameButton.getScene().getWindow();
        curStage.close();
    }

    /**
     * Displays the options of actions the player can do once
     * they click on a regular development card (not orient).
     *
     * @throws IOException IOException if fxml file not found
     */
    @FXML
    protected void onDevelopmentCardShapeClick() throws IOException {
        App.setRootWithSizeTitle("splendor_card_action", 360, 170, "Make your decision");

    }

    /**
     * The logic to handle player purchasing a regular development card (not orient).
     */
    @FXML
    protected void madePurchase() {
        Stage curStage = (Stage) purchaseContent.getScene().getWindow();
        // TODO: implement the logic of handling purchase (fail or success)
        curStage.close();
    }

    /**
     * The logic to handle player close the pop-up Stage when they want to cancel their action.
     */
    @FXML
    protected void cancelAction() {
        Stage curStage = (Stage) purchaseContent.getScene().getWindow();
        // Just close the window without doing anything
        curStage.close();

    }

    /**
     * The logic to handle Reserving Card (both orient and normal card can use this method).
     */
    @FXML
    protected void madeReserve() {
        Stage curStage = (Stage) purchaseContent.getScene().getWindow();
        //TODO: Check condition if the reserve can be done successfully
        // then close the window
        curStage.close();
    }
}
