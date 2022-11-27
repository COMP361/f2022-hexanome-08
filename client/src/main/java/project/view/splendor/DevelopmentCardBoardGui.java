package project.view.splendor;

import java.util.List;
import javafx.scene.image.ImageView;
import project.view.splendor.gameitems.DevelopmentCard;

public interface DevelopmentCardBoardGui {
  void setup(List<DevelopmentCard> cards);
  List<ImageView> getAllCardsGui();
  ImageView getOneCardGui(int cardIndex);
}
