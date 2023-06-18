package project.controllers.popupcontrollers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import project.App;


public class UploadImagePopUpController implements Initializable {

  @FXML
  private ImageView previewImageView;

  @FXML
  private Button confirmUploadButton;

  @FXML
  private Button resetUploadButton;

  @FXML
  private VBox imageDropInVbox;

  private File imageFile = null;


  /**
   * load a file into the image view chosen.
   *
   * @param file
   * @param imageView
   */
  private void loadFile(File file, ImageView imageView) {
    try {
      if (file.getName().endsWith(".png") ||
          file.getName().endsWith(".jpg") ||
          file.getName().endsWith(".jpeg")) {
        Image image = new Image(new FileInputStream(file));
        imageView.setImage(image);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // display zone for the image
    imageDropInVbox.setOnDragOver(event -> {
      if (event.getDragboard().hasFiles()) {
        event.acceptTransferModes(TransferMode.COPY);
      } else {
        event.consume();
      }
    });

    // enable drop-in way of uploading picture
    imageDropInVbox.setOnDragDropped((DragEvent event) -> {
      Dragboard dragboard = event.getDragboard();
      if (dragboard.hasFiles()) {
        try {
          imageFile = dragboard.getFiles().get(0);
          // get the first file from drag board and set it in image view
          loadFile(imageFile, previewImageView);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    // make the vbox clickable so that one can choose to upload from file system
    imageDropInVbox.setOnMouseClicked(event -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.getExtensionFilters().add(
          new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

      File file = fileChooser.showOpenDialog(App.getCurrentPopupStage());
      if (file != null) {
        loadFile(file, previewImageView);
      }
    });

    // TODO: confirm button behavior
    confirmUploadButton.setOnAction(uploadEvent -> {
      if (imageFile != null) {
        // do something with the file if it exists
      }
    });

    resetUploadButton.setOnAction(event -> {
      // reset image file to null
      imageFile = null;
      // reset image view
      previewImageView.setImage(null);
    });


  }
}
