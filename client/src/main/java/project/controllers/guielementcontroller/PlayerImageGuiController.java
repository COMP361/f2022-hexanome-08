package project.controllers.guielementcontroller;

import ca.mcgill.comp361.splendormodel.model.PlayerInGame;
import ca.mcgill.comp361.splendormodel.model.PlayerStates;
import ca.mcgill.comp361.splendormodel.model.SplendorDevHelper;
import com.mashape.unirest.http.HttpResponse;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import project.App;
import project.connection.GameRequestSender;
import project.controllers.popupcontrollers.PurchaseHandController;

/**
 * PlayerImageGuiController.
 */
public class PlayerImageGuiController implements Initializable {

  private final String playerName;
  private final int armCode;
  private final long gameId;
  @FXML
  private Text maxPointsText;
  @FXML
  private Text currentPointsText;
  @FXML
  private Text reservedNobleCountText;
  @FXML
  private Text reservedCardsCountText;
  @FXML
  private Rectangle highlightRectangle;
  @FXML
  private ImageView armImageView;
  @FXML
  private ImageView playerImageView;
  @FXML
  private Circle reservedInfoCircle;
  @FXML
  private Circle pointsCircle;

  @FXML
  private Label playerNameLabel;

  /**
   * PlayerImageGuiController.
   *
   * @param gameId gameId
   * @param playerName playerName
   * @param armCode armCode
   */
  public PlayerImageGuiController(long gameId, String playerName, int armCode) {
    this.gameId = gameId;
    this.playerName = playerName;
    this.armCode = armCode;
  }

  /**
   * getCurrentPointsText.
   *
   * @return text
   */
  public Text getCurrentPointsText() {
    return currentPointsText;
  }

  public Text getReservedNobleCountText() {
    return reservedNobleCountText;
  }

  /**
   * getReservedCardsCountText.
   *
   * @return text
   */
  public Text getReservedCardsCountText() {
    return reservedCardsCountText;
  }

  /**
   * getHighlightRectangle.
   *
   * @return Rectangle
   */
  public Rectangle getHighlightRectangle() {
    return highlightRectangle;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    if (armCode > 0) {
      armImageView.setImage(new Image(App.getArmPath(armCode)));
    }
    playerImageView.setImage(App.getPlayerImage(playerName));

    playerNameLabel.setText(playerName);
    String tipInfo = "The number on the left is the number of reserved nobles\n"
        + "The number on the right is the number of reserved cards";
    App.bindToolTip(tipInfo, 15, reservedInfoCircle, 20);

    String pointsInfo = "Player's prestige points out of 15";
    App.bindToolTip(pointsInfo, 15, pointsCircle, 20);

    String playerImageViewTip = "Click on to see the player's purchased hand!";
    // only bind the tooltip except the current user's image view
    if (!App.getUser().getUsername().equals(playerName)) {
      App.bindToolTip(playerImageViewTip, 15, playerImageView, 20);
      playerImageView.setOnMouseClicked(e -> {
        GameRequestSender sender = App.getGameRequestSender();
        HttpResponse<String> response = sender.sendGetAllPlayerInfoRequest(gameId, "");
        PlayerStates playerStates = SplendorDevHelper.getInstance().getGson()
            .fromJson(response.getBody(), PlayerStates.class);
        PlayerInGame playerInGame = playerStates.getOnePlayerInGame(playerName);
        App.loadPopUpWithController("purchase_hand.fxml",
            new PurchaseHandController(gameId, playerInGame, new HashMap<>()),
            App.getGuiLayouts().getLargePopUpWidth(),
            App.getGuiLayouts().getLargePopUpHeight());
      });
    }

  }
}
