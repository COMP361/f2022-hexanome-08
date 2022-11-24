package project.view.splendor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

public class TokenBankGui extends HBox {

  public TokenBankGui() {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/token_bank.fxml"));
    //fxmlLoader.setRoot(this);
    //fxmlLoader.setController(this);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
