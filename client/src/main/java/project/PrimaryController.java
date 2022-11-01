package project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/** The PrimaryController use to manage the general flow of the program. */
public class PrimaryController {

<<<<<<< Updated upstream
  @FXML
  private ChoiceBox<String> gameChoices;

  @FXML
  private Pane purchaseContent;

  @FXML
  private BorderPane waitingRoom;

  @FXML
  private Pane confirmPane;

  @FXML
  private Button quitGameButton;

  @FXML
  private TextField userName;


  @FXML
  private PasswordField userPassword;

  @FXML
  private BorderPane lobbyPane;

  @FXML
  private Label logInPageErrorMessage;

  @FXML
  private Button confirmButton;

  @FXML
  private Button backButton;

  @FXML
  private Pane resCardPane;

  @FXML
  private Pane devCardPane;


  @FXML
  public Button plusR;
=======
  @FXML public Button plusR;
>>>>>>> Stashed changes
  public Button plusW;
  public Button plusB;
  @FXML public Button minusR;
  public Button minusW;
  public Button minusB;
  @FXML public Text counterRed = new Text();
  public Text counterWhite = new Text();
  public Text counterBlack = new Text();
  @FXML public Text totalRed = new Text();
  public Text totalWhite = new Text();
  public Text totalBlack = new Text();
  public int numR = 0;
  public int num2R = 7;
  public int numW = 0;
  public int num2W = 7;
  public int numB = 0;
  public int num2B = 7;
  @FXML private ChoiceBox<String> gameChoices;
  @FXML private Pane purchaseContent;
  @FXML private BorderPane waitingRoom;
  @FXML private Pane confirmPane;
  @FXML private Button quitGameButton;
  @FXML private TextField userName;
  @FXML private ImageView purchasedCard;
  @FXML private PasswordField userPassword;
  @FXML private BorderPane lobbyPane;
  @FXML private Label logInPageErrorMessage;
  @FXML private Button confirmButton;
  @FXML private Button backButton;
  @FXML private Pane resCardPane;
  @FXML private Pane devCardPane;
  @FXML private MenuItem exitGame;
  @FXML private MenuItem exitWaitingRoom;
  @FXML private MenuItem logOutFromWaitingRoom;
  @FXML private MenuItem logOutFromLobby;
  @FXML private Button confirmExitGame;
  @FXML private Button backExitGame;
  @FXML private Menu gameMenu;
  private Stage helper;


  @FXML
  public Text redHand = new Text();
  public Text whiteHand = new Text();
  public Text blackHand = new Text();

  @FXML
  public Pane reserveConfirmPane;


  /**
   * The logic of handling log in. The methods check if the user has input both username and user
   * password or not
   *
   * @throws IOException IOException if fxml file not found
   */
  @FXML
  protected void onLogInButtonClick() throws IOException {
    String userNameStr = userName.getText();
    String userPasswordStr = userPassword.getText();

    // For the sake of simplicity, we only check if password and username exist
    if (Objects.equals(userNameStr, "") || Objects.equals(userPasswordStr, "")) {
      logInPageErrorMessage.setText("Please enter both valid username and password");
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

  /** Close the current stage once the quitGameButton has been clicked. */
  @FXML
  protected void onQuitGameButtonClick() throws IOException {
    Stage curStage = (Stage) quitGameButton.getScene().getWindow();
    //App.setRoot("LobbyService");
    curStage.close();
  }

  /** Produce confirm pop-up once said menu item has been selected. */
  @FXML
  protected void onExitGameMenuClick() throws IOException {
    helper = (Stage) exitGame.getParentPopup().getOwnerWindow();
    App.setRootWithSizeTitle("LobbyService", 1100, 800, "Lobby Service");
    helper.close();
    //App.setRootWithSizeTitle("exit_game_confirm", 300, 150, "Confirm exit");

  }

  /** Exit game once said confirmed in popup has been selected. */
  @FXML
  protected void onConfirmExitGame() throws IOException {
    helper.close();
    Stage curStage2 = (Stage) confirmExitGame.getScene().getWindow();
    curStage2.close();
    App.setRootWithSizeTitle("LobbyService", 1100, 800, "Lobby Service");
  }

  /** Exit game once said confirmed in popup has been selected. */
  @FXML
  protected void onBackGameExit() throws IOException {
    Stage curStage = (Stage) backExitGame.getScene().getWindow();
    curStage.close();
  }

  /** Exit waiting room once said menu item has been selected. */
  @FXML
  protected void onExitWaitingRoomMenu() throws IOException {
    Stage curStage = (Stage) exitWaitingRoom.getParentPopup().getOwnerWindow();
    App.setRootWithSizeTitle("LobbyService", 1100, 800, "Lobby Service");
    curStage.close();
  }

  /** Log out from waiting room once said menu item has been selected. */
  @FXML
  protected void onLogOutFromWaitingRoomMenu() throws IOException {
    Stage curStage = (Stage) logOutFromWaitingRoom.getParentPopup().getOwnerWindow();
    App.setRootWithSizeTitle("splendor", 1000, 800, "Splendor");
    curStage.close();
  }

  /** Log out from lobby once said menu item has been selected. */
  @FXML
  protected void onLogOutFromLobbyMenu() throws IOException {
    Stage curStage = (Stage) logOutFromLobby.getParentPopup().getOwnerWindow();
    App.setRootWithSizeTitle("splendor", 1000, 800, "Splendor");
    curStage.close();
  }

  /**
   * Displays the options of actions the player can do once they click on a regular development card
   * (not orient).
   *
   * @throws IOException IOException if fxml file not found
   */
  @FXML
  protected void onDevelopmentCardShapeClick() throws IOException {
    App.setRootWithSizeTitle("splendor_card_action", 360, 170, "Make your decision");
  }

  /** The logic to handle player purchasing a regular development card (not orient). */
  @FXML
  protected void madePurchase() throws IOException {
    App.setPopUpRoot("splendor_purchase_confirm", purchaseContent.getScene());
  }

  /** The logic to handle player close the pop-up Stage when they want to cancel their action. */
  @FXML
  protected void cancelAction() {
    Stage curStage = (Stage) purchaseContent.getScene().getWindow();
    // Just close the window without doing anything
    curStage.close();
  }

<<<<<<< Updated upstream

  /**
   * The logic to handle Reserving Card (both orient and normal card can use this method).
   */
=======
  @FXML
  protected void purchased() throws FileNotFoundException {
    InputStream stream = new FileInputStream("src/main/resources/project/pictures/level3/w1.png");
    Image img = new Image(stream);
    purchasedCard.setImage(img);
  }

  /** The logic to handle Reserving Card (both orient and normal card can use this method). */
>>>>>>> Stashed changes
  @FXML
  protected void madeReserve() throws IOException {
    // Stage curStage = (Stage) purchaseContent.getScene().getWindow();
    Scene curScene = purchaseContent.getScene();
    App.setPopUpRoot("splendor_reserve", curScene);
    // TODO: Check condition if the reserve can be done successfully
    // then close the window

  }

  @FXML
  protected void joinGame() throws IOException {
    App.setRoot("splendor_game_board");
    App.setHandCard();
    App.setReserveCard();
    Stage curStage = (Stage) waitingRoom.getScene().getWindow();
    curStage.close();
<<<<<<< Updated upstream
    curStage = (Stage) App.getScene().getWindow();
    curStage.show();

=======
>>>>>>> Stashed changes
  }

  @FXML
  protected void joinWaitingRoom() throws IOException {
    Stage curStage = (Stage) App.getScene().getWindow();
    curStage.close();
    App.setRootWithSizeTitle("splendor_waiting_room", 1000, 500, "Waiting Room");
  }


  @FXML
  protected void backToLobby() {
    Stage curStage = (Stage) waitingRoom.getScene().getWindow();
    curStage.close();
    curStage = (Stage) App.getScene().getWindow();
    curStage.show();
  }

  /** Sets up the Choice Box options in Main Lobby. */
  @FXML
  public void dropDownLobby() {
    // Create observable list for game options drop down (choice box)
    ObservableList<String> gameOptionsList =
        FXCollections.observableArrayList("Splendor (Base Game)", "Splendor (Orient Expansion)");
    // gameChoices will be null until the main lobby stage is launched
    if (gameChoices != null) {
      gameChoices.setItems(gameOptionsList);
    }
  }

<<<<<<< Updated upstream

  /**
   * TODO: HARDCODED!
   * Getting rid of the confirmation pop up once "confirm" is pressed when purchasing a card.
   */
=======
  /** Getting rid of the confirmation pop up once "confirm" is pressed when purchasing a card. */
>>>>>>> Stashed changes
  @FXML
  public void confirmClick() throws FileNotFoundException {
    ImageView purchasedCard = (ImageView) App.getScene().lookup("#purchasedCard");
    InputStream stream1 =
            new FileInputStream("src/main/resources/project/pictures/level3/w1.png");
    Image img1 = new Image(stream1);
    purchasedCard.setImage(img1);

    ImageView newCard = (ImageView) App.getHandCard().lookup("#newCard");
    InputStream stream2 =
            new FileInputStream("src/main/resources/project/pictures/level3/b4.png");
    Image img2 = new Image(stream2);
    newCard.setImage(img2);
    Stage curStage = (Stage) confirmPane.getScene().getWindow();
    curStage.close();
  }

<<<<<<< Updated upstream
  /**
   * TODO: HARDCODED!
   * Reserve the pictures/level2/b4.png card, replace it by pictures/level2/w1.png
   */
  @FXML
  public void reserveConfirmClick() throws FileNotFoundException {
    ImageView reservedCard = (ImageView) App.getScene().lookup("#reservedCard");
    InputStream stream1 =
        new FileInputStream("src/main/resources/project/pictures/level2/w1.png");
    Image img1 = new Image(stream1);
    reservedCard.setImage(img1);
    System.out.println(reservedCard.getImage());
    ImageView newReserveCard = (ImageView) App.getReservedCards().lookup("#newReserveCard");
    System.out.println(newReserveCard.getImage());
    InputStream stream2 =
        new FileInputStream("src/main/resources/project/pictures/level2/b4.png");
    Image img2 = new Image(stream2);
    newReserveCard.setImage(img2);
    System.out.println(newReserveCard.getImage());
    Stage curStage = (Stage) reserveConfirmPane.getScene().getWindow();
    curStage.close();
  }

  /**
   * Getting rid of the confirmation pop up once "back" is pressed when purchasing a card.
   */
=======
  /** Getting rid of the confirmation pop up once "back" is pressed when purchasing a card. */
>>>>>>> Stashed changes
  @FXML
  protected void backClick() {
    Stage curStage = (Stage) confirmPane.getScene().getWindow();
    curStage.close();
  }

<<<<<<< Updated upstream
  @FXML
  protected void reserveBackClick() {
    Stage curStage = (Stage) reserveConfirmPane.getScene().getWindow();
    curStage.close();
  }

  /**
   * Getting rid of the development cards pop up once "x" is pressed when purchasing a card.
   */

=======
  /** Getting rid of the development cards pop up once "x" is pressed when purchasing a card. */
>>>>>>> Stashed changes
  public void exitDevCard() {
    Stage curStage = (Stage) devCardPane.getScene().getWindow();
    curStage.close();
  }

<<<<<<< Updated upstream
  /**
   * Opening the development cards pop up once "My Cards" button is pressed.
   */

  public void openMyCards() {
    Stage newStage = new Stage();
    newStage.setTitle("My Development Cards");
    newStage.setScene(App.getHandCard());
    newStage.getIcons().add(new Image("project/pictures/back/splendor-icon.jpg"));
    newStage.show();
=======
  /** Opening the development cards pop up once "My Cards" button is pressed. */
  public void openMyCards() throws IOException {
    App.setRootWithSizeTitle("my_development_cards", 789, 406, "My Development Cards");
>>>>>>> Stashed changes
  }

  /** Opening the reserve card pop up once reserved card button is pressed. */
  public void openMyReservedCards() throws IOException {
    App.setRootWithSizeTitle("my_reserved_cards", 789, 406, "My Reserved Cards");
  }

  /** Getting rid of the reserved cards pop up once "back" is pressed when purchasing a card. */
  public void exitReserved() {
    Stage curStage = (Stage) resCardPane.getScene().getWindow();
    curStage.close();
  }

<<<<<<< Updated upstream
  public int gloCount = 0;
  public int numR = 0;
  public int num2R = 7;
  public int num3R = 0;
  public int numW = 0;
  public int num2W = 7;
  public int num3W = 0;
  public int numB = 0;
  public int num2B = 7;
  public int num3B = 0;


  /**
   * decrement red.
   */
=======
  /** decrement red. */
>>>>>>> Stashed changes
  public void decrementR() {
    if ((numR - 1) >= 0) {
      numR = numR - 1;
      gloCount--;
      counterRed.setText(String.valueOf(numR));
    }
  }

  /** decrement white. */
  public void decrementW() {
    if ((numW - 1) >= 0) {
      gloCount--;
      numW = numW - 1;
      counterWhite.setText(String.valueOf(numW));
    }
  }

  /** decrement black. */
  public void decrementB() {
    if ((numB - 1) >= 0) {
      gloCount--;
      numB = numB - 1;
      counterBlack.setText(String.valueOf(numB));
    }
  }

  /** increment red. */
  public void incrementR() {
    if ((numR + 1) <= num2R && numR < 2 && gloCount < 3) {
      gloCount++;
      numR = numR + 1;
      counterRed.setText(String.valueOf(numR));
    }
  }

  /** increment white. */
  public void incrementW() {
    if ((numW + 1) <= num2W && numW < 2 && numR < 2 && gloCount < 3) {
      gloCount++;
      numW = numW + 1;
      counterWhite.setText(String.valueOf(numW));
    }
  }

  /** increment black. */
  public void incrementB() {
    if ((numB + 1) <= num2B && numB < 2 && numR < 2 && gloCount < 3) {
      gloCount++;
      numB = numB + 1;
      counterBlack.setText(String.valueOf(numB));
    }
  }

  /** confirm/ update total gems. */
  public void gemConfirm() {
    gloCount = 0;
    num2R = num2R - numR;
    num3R = num3R + numR;
    totalRed.setText(String.valueOf(num2R));
    numR = 0;
    counterRed.setText(String.valueOf(numR));
    redHand.setText(String.valueOf(num3R) + " / 0");

    num2W = num2W - numW;
    num3W = num3W + numW;
    totalWhite.setText(String.valueOf(num2W));
    numW = 0;
    counterWhite.setText(String.valueOf(numW));
    whiteHand.setText(String.valueOf(num3W) + " / 0");


    num2B = num2B - numB;
    num3B = num3B + numB;
    totalBlack.setText(String.valueOf(num2B));
    numB = 0;
    counterBlack.setText(String.valueOf(numB));
    blackHand.setText(String.valueOf(num3B) + " / 0");
  }
}
