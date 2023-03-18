package project.controllers.popupcontrollers;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.ClaimNobleAction;
import ca.mcgill.comp361.splendormodel.model.NobleCard;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import project.App;
import project.connection.GameRequestSender;

/**
 * Control claim noble pop up.
 */
public class ClaimNoblePopUpController implements Initializable {

  private final long gameId;
  private final Map<String, Action> playerActionMap;
  @FXML
  private HBox unlockedNoblesHbox;

  public ClaimNoblePopUpController(long gameId, Map<String, Action> playerActionMap) {
    this.gameId = gameId;
    this.playerActionMap = playerActionMap;
  }


  private EventHandler<MouseEvent> createClickOnNobleToClaim(String actionId) {
    return event -> {
      GameRequestSender gameRequestSender = App.getGameRequestSender();
      String playerName = App.getUser().getUsername();
      String accessToken = App.getUser().getAccessToken();
      // sends a POST request that tells the server which action we chose
      gameRequestSender.sendPlayerActionChoiceRequest(gameId, playerName, accessToken, actionId);

      ImageView imageView = (ImageView) event.getSource();
      Stage curWindow = (Stage) imageView.getScene().getWindow();
      curWindow.close();
    };
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    for (String actionId : playerActionMap.keySet()) {
      Action action = playerActionMap.get(actionId);
      ClaimNobleAction claimNobleAction = (ClaimNobleAction) action;
      NobleCard nobleCard = claimNobleAction.getCurCard();
      Image nobleImage = new Image(App.getNoblePath(nobleCard.getCardName()));
      ImageView nobleImageView = new ImageView(nobleImage);
      nobleImageView.setFitHeight(100);
      nobleImageView.setFitWidth(80);
      nobleImageView.setOnMouseClicked(createClickOnNobleToClaim(actionId));
      unlockedNoblesHbox.getChildren().add(nobleImageView);
    }

  }
}
