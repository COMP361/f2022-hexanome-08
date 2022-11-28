package project.view.splendor;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class NobleListGui extends VBox {

  public NobleListGui(List<ImageView> nobleImages) {
    for (ImageView img : nobleImages) {
      this.getChildren().add(img);
    }
  }
}
