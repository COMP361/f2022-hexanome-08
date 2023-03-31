package project.controllers.popupcontrollers;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.ClaimCityAction;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import project.App;

public class ClaimCityPopUpController extends ActionSelectionSender implements Initializable {

  @FXML
  private VBox unlockedCityCardsVbox;
  private final Map<String, Action> playerActionMap;

  public ClaimCityPopUpController(long gameId, Map<String, Action> playerActionMap) {
    super(gameId);
    this.playerActionMap = playerActionMap;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    for (String actionId : playerActionMap.keySet()) {
      Action action = playerActionMap.get(actionId);
      ClaimCityAction claimCityAction = (ClaimCityAction) action;
      String cityImagePath = App.getCityPath(claimCityAction.getCityCard());
      ImageView cityImageView = new ImageView(new Image(cityImagePath));
      cityImageView.setOnMouseClicked(createOnActionSelectionClick(actionId));
      cityImageView.setFitHeight(App.getGuiLayouts().getCityHeight());
      cityImageView.setFitWidth(App.getGuiLayouts().getCityWidth());
      unlockedCityCardsVbox.getChildren().add(cityImageView);
    }

  }
}
