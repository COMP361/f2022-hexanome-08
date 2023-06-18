package project.controllers.popupcontrollers;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import org.apache.commons.io.FileUtils;
import project.App;
import project.config.ConnectionConfig;

public class AppSettingPageController implements Initializable {

  private final ConnectionConfig connectionConfig;

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



  public AppSettingPageController(ConnectionConfig connectionConfig) {
    this.connectionConfig = connectionConfig;

  }

  private void updateConnectionConfigFile() {
    try {
      FileUtils.writeStringToFile(
          App.getConnectionConfigFile(),
          new Gson().toJson(connectionConfig, ConnectionConfig.class)
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // the update buttons should be re-activated or greyed out accordingly
    defaultUserNameUpdateButton.setDisable(!connectionConfig.isUseDefaultUserInfo());
    defaultPasswordUpdateButton.setDisable(!connectionConfig.isUseDefaultUserInfo());

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

      // if the info after being acted on has changed, update the file
      if (connectionConfig.isUseDefaultUserInfo() != inUse) {
        connectionConfig.setUseDefaultUserInfo(inUse);
        updateConnectionConfigFile();
      }
    });
    defaultUserNameTextField.setPromptText(connectionConfig.getDefaultUserName());
    defaultPasswordField.setPromptText(connectionConfig.getDefaultPassword());
    // username update logic
    defaultUserNameUpdateButton.setOnAction(event -> {
      // if text input is not null or empty string, overwrite the old username
      if (defaultUserNameTextField.getText() != null &&
          !defaultUserNameTextField.getText().isEmpty()) {
        connectionConfig.setDefaultUserName(defaultUserNameTextField.getText());
        updateConnectionConfigFile();
        defaultUserNameTextField.clear();
        defaultUserNameTextField.setPromptText(connectionConfig.getDefaultUserName());
      }
    });

    // user password update logic
    defaultPasswordUpdateButton.setOnAction(event -> {
      if (defaultPasswordField.getText() != null &&
          !defaultPasswordField.getText().isEmpty()) {
        connectionConfig.setDefaultPassword(defaultPasswordField.getText());
        updateConnectionConfigFile();
        defaultPasswordField.clear();
        defaultPasswordField.setPromptText(connectionConfig.getDefaultPassword());
      }

    });


    // the update button for host ip
    hostIpUpdateButton.setDisable(connectionConfig.isUseLocalHost());
    hostIpUpdateButton.setOnAction(event -> {
      if (defaultHostIpTextField.getText() != null &&
      !defaultHostIpTextField.getText().isEmpty()) {
        connectionConfig.setHostIp(defaultHostIpTextField.getText());
        updateConnectionConfigFile();
      }
      defaultHostIpTextField.clear();
      defaultHostIpTextField.setPromptText(connectionConfig.getHostIp());
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

      if (connectionConfig.isUseLocalHost() != usingHostIp) {
        connectionConfig.setUseLocalHost(usingHostIp);
        updateConnectionConfigFile();
      }
      defaultHostIpTextField.setPromptText(connectionConfig.getHostIp());
    });

    defaultHostIpTextField.setPromptText(connectionConfig.getHostIp());

  }
}
