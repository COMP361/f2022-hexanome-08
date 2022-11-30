package project.view.splendor;

import java.util.List;
import javafx.scene.image.ImageView;

public interface DevelopmentCardBoardGui {

  void setup();

  List<ImageView> getAllCardsGui();

  ImageView getOneCardGui(int cardIndex);
}
