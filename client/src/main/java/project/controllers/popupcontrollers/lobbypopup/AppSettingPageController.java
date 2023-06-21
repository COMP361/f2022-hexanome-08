package project.controllers.popupcontrollers.lobbypopup;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import project.App;
import project.config.UserConfig;

public class AppSettingPageController implements Initializable {

  private final UserConfig userConfig;

  @FXML
  private ToggleButton defaultLogInToggleButton;

  @FXML
  private ToggleButton localIpToggleButton;

  @FXML
  private TextField defaultUserNameTextField;

  @FXML
  private Button defaultUserNameUpdateButton;

  @FXML
  private PasswordField defaultPasswordField;

  @FXML
  private Button defaultPasswordUpdateButton;

  @FXML
  private TextField defaultHostIpTextField;

  @FXML
  private Button hostIpUpdateButton;

  @FXML
  private Button backToLogInButton;

  @FXML
  private Slider musicVolumeSlider;

  @FXML
  private TextField musicVolumeTextField;

  @FXML
  private AnchorPane backgroundPane;


  public AppSettingPageController(UserConfig userConfig) {
    this.userConfig = userConfig;

  }

  private void updateUserConfigFile() {
    try {
      FileUtils.writeStringToFile(
          App.getConnectionConfigFile(),
          new Gson().toJson(userConfig, UserConfig.class)
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  private void setupDefaultLoginSettings() {
    // the update buttons should be re-activated or greyed out accordingly
    defaultUserNameUpdateButton.setDisable(!userConfig.isUseDefaultUserInfo());
    defaultPasswordUpdateButton.setDisable(!userConfig.isUseDefaultUserInfo());

    defaultLogInToggleButton.setSelected(userConfig.isUseDefaultUserInfo());
    if (defaultLogInToggleButton.isSelected()) {
      defaultLogInToggleButton.setText("ON");
    } else {
      defaultLogInToggleButton.setText("OFF");
    }

    // bind actions to default log in section
    defaultLogInToggleButton.setOnAction(event -> {
      boolean inUse = defaultLogInToggleButton.isSelected();
      // if it's selected, the text display is ON, otherwise is OFF
      if (inUse) {
        defaultLogInToggleButton.setText("ON");
      } else {
        defaultLogInToggleButton.setText("OFF");
      }

      // the update buttons should be re-activated or greyed out accordingly
      defaultUserNameUpdateButton.setDisable(!inUse);
      defaultPasswordUpdateButton.setDisable(!inUse);

      userConfig.setUseDefaultUserInfo(inUse);
      updateUserConfigFile();
    });
    defaultUserNameTextField.setPromptText(userConfig.getDefaultUserName());
    defaultPasswordField.setPromptText(userConfig.getDefaultPassword());
    // username update logic
    defaultUserNameUpdateButton.setOnAction(event -> {
      // if text input is not null or empty string, overwrite the old username
      if (defaultUserNameTextField.getText() != null &&
          !defaultUserNameTextField.getText().isEmpty()) {
        userConfig.setDefaultUserName(defaultUserNameTextField.getText());
        updateUserConfigFile();
        defaultUserNameTextField.clear();
        defaultUserNameTextField.setPromptText(userConfig.getDefaultUserName());
      }
    });

    // user password update logic
    defaultPasswordUpdateButton.setOnAction(event -> {
      if (defaultPasswordField.getText() != null &&
          !defaultPasswordField.getText().isEmpty()) {
        userConfig.setDefaultPassword(defaultPasswordField.getText());
        updateUserConfigFile();
        defaultPasswordField.clear();
        defaultPasswordField.setPromptText(userConfig.getDefaultPassword());
      }

    });
  }

  private void setupDefaultIpSettings() {
    localIpToggleButton.setSelected(userConfig.isUseLocalHost());
    if (localIpToggleButton.isSelected()) {
      localIpToggleButton.setText("ON");
    } else {
      localIpToggleButton.setText("OFF");
    }

    defaultHostIpTextField.setPromptText(userConfig.getHostIp());


    // the update button for host ip
    hostIpUpdateButton.setDisable(userConfig.isUseLocalHost());
    hostIpUpdateButton.setOnAction(event -> {
      if (defaultHostIpTextField.getText() != null &&
          !defaultHostIpTextField.getText().isEmpty()) {
        userConfig.setHostIp(defaultHostIpTextField.getText());
        updateUserConfigFile();
      }
      defaultHostIpTextField.clear();
      defaultHostIpTextField.setPromptText(userConfig.getHostIp());
    });


    // the toggle button of using local ip update functionality or not
    localIpToggleButton.setOnAction(event -> {
      boolean usingHostIp = localIpToggleButton.isSelected();
      if (usingHostIp) {
        localIpToggleButton.setText("ON");
      } else {
        localIpToggleButton.setText("OFF");
      }

      hostIpUpdateButton.setDisable(usingHostIp);

      userConfig.setUseLocalHost(usingHostIp);
      updateUserConfigFile();
      defaultHostIpTextField.setPromptText(userConfig.getHostIp());
    });

  }

  private void setupMusicVolumeSettings() {
    MediaPlayer mediaPlayer = App.getGameMusicPlayer();
    musicVolumeSlider.setMin(0);
    musicVolumeSlider.setMax(100);
    // change volume through sliding a slider
    musicVolumeSlider.valueProperty().addListener(observable -> {
      // slider has value changing from 0 -> 100
      double displayVolume = musicVolumeSlider.getValue();
      double actualVolumeValue = displayVolume / 100;

      // after the volume has been changed, save it to user config file
      mediaPlayer.setVolume(actualVolumeValue);
      userConfig.setMusicVolume(actualVolumeValue);
      updateUserConfigFile();
      musicVolumeTextField.setText(String.format("%.0f", displayVolume));
    });

    // default volume ratio
    mediaPlayer.setVolume(userConfig.getMusicVolume());
    musicVolumeSlider.setValue(mediaPlayer.getVolume() * 100);
    musicVolumeTextField.setText(String.format("%.0f", musicVolumeSlider.getValue()));

    // change volume through entering number
    musicVolumeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      // check if the new value is an integer
      if (!newValue.matches("\\d*")) {
        // remove non-digits
        musicVolumeTextField.setText(newValue.replaceAll("[^\\d]", ""));
      }
      // check if the integer is in range
      try {
        int newVolume = Integer.parseInt(musicVolumeTextField.getText());
        if (newVolume < 1 || newVolume > 100) {
          // if not in range, revert to the old value
          musicVolumeTextField.setText(oldValue);
        } else {
          // it is indeed in range, update the slider value
          musicVolumeSlider.setValue(newVolume);
        }
      } catch (NumberFormatException e) {
        musicVolumeTextField.setText("");
      }
    });

  }
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // back to log in page button
    backToLogInButton.setOnAction(event -> {
      Stage curWindow = (Stage) backToLogInButton.getScene().getWindow();
      curWindow.close();
      App.backToLogInPage();
    });

    backgroundPane.setOnMouseClicked(event -> {backgroundPane.requestFocus();});
    setupDefaultLoginSettings();
    setupDefaultIpSettings();
    setupMusicVolumeSettings();

  }
}
