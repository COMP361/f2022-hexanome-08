package project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The PrimaryController use to manage the general flow of the program.
 */
public class PrimaryController {


  @FXML
  private ChoiceBox<String> gameChoices;

  @FXML
  private Pane purchaseContent;

  @FXML
  private Pane waitingRoom;

  @FXML
  private Pane confirmPane;

  @FXML
  private Button quitGameButton;

  @FXML
  private TextField userName;

  @FXML
  public ImageView purchasedCard;

  @FXML
  private PasswordField userPassword;

  @FXML
  private Label logInPageErrorMessage;

  @FXML
  private Button Confirm;

  @FXML
  private Button Back;

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
      App.setRoot("LobbyService");
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
  protected void madePurchase() throws IOException {
    App.setPopUpRoot("splendor_purchase_confirm", purchaseContent.getScene());
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


  @FXML
  protected void purchased() throws FileNotFoundException {
    InputStream stream = new FileInputStream("client/src/main/resources/project/pictures/level3/w1.png");
    Image img = new Image(stream);
    purchasedCard.setImage(img);
  }

  /**
   * The logic to handle Reserving Card (both orient and normal card can use this method).
   */
  @FXML
  protected void madeReserve() throws IOException {
    //Stage curStage = (Stage) purchaseContent.getScene().getWindow();
    Scene curScene = purchaseContent.getScene();
    App.setPopUpRoot("splendor_reserve", curScene);
    //TODO: Check condition if the reserve can be done successfully
    // then close the window

  }

  @FXML
  protected void joinGame() throws IOException {
    Stage curStage = (Stage) waitingRoom.getScene().getWindow();
    App.setRootWithSizeTitle("splendor_game_board", 1100, 800, "Splendor Game");
    curStage.close();
  }

  @FXML
  protected void joinWaitingRoom() throws IOException {
    App.setRootWithSizeTitle("splendor_waiting_room", 1000, 500, "Waiting Room");
  }

  /**
   * Sets up the Choice Box options in Main Lobby.
   */
  @FXML
  public void initialize() {
    //Create observable list for game options drop down (choice box)
    ObservableList<String> gameOptionsList = FXCollections
            .observableArrayList("Splendor (Base Game)", "Splendor (Orient Expansion)");
    //gameChoices will be null until the main lobby stage is launched
    if (gameChoices != null) {
      gameChoices.setItems(gameOptionsList);
    }
  }


  /**
   * Getting rid of the confirmation pop up once "confirm" is pressed when purchasing a card
   */
  @FXML
  public void confirmClick() throws IOException {
    Stage curStage = (Stage) confirmPane.getScene().getWindow();
    curStage.close();
  }

  /**
   * Getting rid of the confirmation pop up once "back" is pressed when purchasing a card
   */
  @FXML
  protected void backClick(){
    Stage curStage = (Stage) confirmPane.getScene().getWindow();
    curStage.close();
  }




}
