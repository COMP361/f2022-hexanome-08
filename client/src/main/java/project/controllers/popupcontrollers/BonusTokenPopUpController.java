package project.controllers.popupcontrollers;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.BonusTokenPowerAction;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import project.App;

public class BonusTokenPopUpController extends ActionSelectionSender implements Initializable {

  private final Map<String, Action> playerActionMap;
  @FXML
  private HBox availableTokensHbox;


  public BonusTokenPopUpController(long gameId, Map<String, Action> playerActionMap) {
    super(gameId);
    this.playerActionMap = playerActionMap;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    for (String actionId : playerActionMap.keySet()) {
      Action action = playerActionMap.get(actionId);
      BonusTokenPowerAction bonusTokenPowerAction = (BonusTokenPowerAction) action;
      String tokenImagePath = App.getTokenPath(bonusTokenPowerAction.getColour());
      ImageView tokenImageView = new ImageView(new Image(tokenImagePath));
      tokenImageView.setOnMouseClicked(createOnActionSelectionClick(actionId));
      tokenImageView.setFitHeight(100);
      tokenImageView.setFitWidth(100);
      availableTokensHbox.getChildren().add(tokenImageView);
    }
  }
}
