package project.controllers.popupcontrollers;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.CardExtraAction;
import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
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
 * Control free card pop up.
 */
public class FreeCardPopUpController implements Initializable {


  @FXML
  private HBox freeCardsHbox;

  private final long gameId;

  private final Map<String, Action> playerActionMap;

  public FreeCardPopUpController(long gameId, Map<String, Action> playerActionMap) {
    this.gameId = gameId;
    this.playerActionMap = playerActionMap;
  }

  private EventHandler<MouseEvent> createClickOnFreeCardToTake(String actionId) {
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
      CardExtraAction cardExtraAction = (CardExtraAction) action;
      DevelopmentCard card = (DevelopmentCard) cardExtraAction.getCurCard();
      String cardImagePath;
      if (card.isBaseCard()) {
        cardImagePath = App.getBaseCardPath(card.getCardName(), card.getLevel());
      } else {
        cardImagePath = App.getOrientCardPath(card.getCardName(), card.getLevel());
      }
      ImageView cardImageView = new ImageView(new Image(cardImagePath));
      cardImageView.setFitHeight(100);
      cardImageView.setFitWidth(80);
      cardImageView.setOnMouseClicked(createClickOnFreeCardToTake(actionId));
      freeCardsHbox.getChildren().add(cardImageView);
    }
  }
}
