package project.view.splendor.boardgui;

import ca.mcgill.comp361.splendormodel.model.Position;
import java.util.List;
import java.util.Map;
import javafx.scene.image.ImageView;

/**
 * Interface for all development card GUI classes.
 */
public interface DevelopmentCardBoardGui {

  /**
   * set up the board.
   */
  void setup();

  /**
   * getAllCardsGui.
   *
   * @return List
   */
  List<ImageView> getAllCardsGui();

  /**
   * getOneCardGui.
   *
   * @param cardIndex cardIndex
   * @return imageVIEW
   */
  ImageView getOneCardGui(int cardIndex);

  /**
   * bindActionToCardAndDeck.
   *
   * @param positionToActionMap positionToActionMap
   * @param gameId gameId
   */
  void bindActionToCardAndDeck(Map<Position, List<ActionIdPair>> positionToActionMap, long gameId);
}
