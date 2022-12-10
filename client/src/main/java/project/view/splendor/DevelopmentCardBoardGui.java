package project.view.splendor;

import java.util.List;
import javafx.scene.image.ImageView;

/**
 * Interface for all development card GUI classes.
 */
public interface DevelopmentCardBoardGui {

  void setup();

  List<ImageView> getAllCardsGui();

  ImageView getOneCardGui(int cardIndex);

  void bindActionToCardAndDeck(String[][] actionHashLookUp, long gameId);
}
