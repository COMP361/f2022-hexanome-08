package project.view.lobby;

/**
 * User class used to store user info locally.
 */
public class User {

  private final String username;
  private final String accessToken;
  private final String authority;

  /**
   * Constructor of a user instance.
   *
   * @param pusername username
   * @param paccessToken user access token
   * @param pauthority user authority
   */
  public User(String pusername, String paccessToken, String pauthority) {
    username = pusername;
    accessToken = paccessToken;
    authority = pauthority;
  }

  public String getUsername() {
    return username;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getAuthority() {
    return authority;
  }

}
