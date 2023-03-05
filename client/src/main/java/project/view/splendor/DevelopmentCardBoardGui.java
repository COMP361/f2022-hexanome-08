package project.view.splendor;

import ca.mcgill.comp361.splendormodel.model.Position;
import java.util.List;
import java.util.Map;
import javafx.scene.image.ImageView;

/**
 * Interface for all development card GUI classes.
 */
public interface DevelopmentCardBoardGui {

  void setup();

  List<ImageView> getAllCardsGui();

  ImageView getOneCardGui(int cardIndex);

  void bindActionToCardAndDeck(Map<Position, List<ActionIdPair>> positionToActionMap, long gameId);
}
