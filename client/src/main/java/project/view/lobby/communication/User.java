package project.view.lobby.communication;

import javafx.scene.image.Image;
import project.App;

/**
 * User class used to store user info locally.
 */
public class User {

  private final String username;
  private final String refreshToken;
  private final String authority;
  private String accessToken;

  private Image playerImage = null;

  /**
   * Constructor of a user instance.
   *
   * @param username    username
   * @param accessToken user access token
   * @param refreshToken refresh Token
   * @param authority   user authority
   */
  public User(String username, String accessToken, String refreshToken, String authority) {
    this.username = username;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.authority = authority;
  }

  /**
   * getUsername.
   *
   * @return string of user name
   */
  public String getUsername() {
    return username;
  }

  /**
   * getAccessToken.
   *
   * @return string
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * setAccessToken.
   *
   * @param newAccessToken newAccessToken
   */
  public void setAccessToken(String newAccessToken) {
    accessToken = newAccessToken;
  }

  /**
   * getAuthority.
   *
   * @return authority
   */
  public String getAuthority() {
    return authority;
  }

  /**
   * getRefreshToken
   * @return token
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * Get the image of the player.
   *
   * @return image of the player.
   */
  public Image getPlayerImage() {
    if (playerImage == null) {
      playerImage = App.getPlayerImage(username);
    }
    return playerImage;
  }
}
