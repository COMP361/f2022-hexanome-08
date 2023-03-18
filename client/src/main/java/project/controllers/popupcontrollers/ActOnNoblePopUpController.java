package project.controllers.popupcontrollers;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.CardExtraAction;
import ca.mcgill.comp361.splendormodel.actions.ClaimNobleAction;
import ca.mcgill.comp361.splendormodel.model.NobleCard;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import project.App;

/**
 * Control claim noble pop up.
 */
public class ActOnNoblePopUpController extends ActionSelectionSender implements Initializable {
  private final Map<String, Action> playerActionMap;
  private final boolean isReserve;
  @FXML
  private HBox availableNoblesHbox;
  @FXML
  private Text nobleActionText;


  public ActOnNoblePopUpController(long gameId, Map<String, Action> playerActionMap,
                                   boolean isReserve) {
    super(gameId);
    this.playerActionMap = playerActionMap;
    this.isReserve = isReserve;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    if (!isReserve) {
      nobleActionText.setText("Choose a noble to claim");
    } else {
      nobleActionText.setText("Choose a noble to reserve");
    }


    for (String actionId : playerActionMap.keySet()) {
      Action action = playerActionMap.get(actionId);
      NobleCard nobleCard;
      if (!isReserve) {
        // if not reserve, then we are claiming noble
        ClaimNobleAction claimNobleAction = (ClaimNobleAction) action;
        nobleCard = claimNobleAction.getCurCard();
      } else {
        // if reserve, then it's a card extra action
        nobleCard = (NobleCard) ((CardExtraAction) action).getCurCard();
      }

      Image nobleImage = new Image(App.getNoblePath(nobleCard.getCardName()));
      ImageView nobleImageView = new ImageView(nobleImage);
      nobleImageView.setFitHeight(100);
      nobleImageView.setFitWidth(80);
      nobleImageView.setOnMouseClicked(createOnActionSelectionClick(actionId));
      availableNoblesHbox.getChildren().add(nobleImageView);
    }

  }
}
