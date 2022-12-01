package project.view.splendor;

import java.util.List;
import javafx.scene.image.ImageView;
import project.view.splendor.communication.Position;

public interface DevelopmentCardBoardGui {

  void setup();

  List<ImageView> getAllCardsGui();

  ImageView getOneCardGui(int cardIndex);

  void bindActionToCardAndDeck(String[][] actionHashLookUp, long gameId);
}
