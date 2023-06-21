package project.view.lobby.communication;


/**
 * Communication class needed to update player information to LS.
 */
public class Player {

  private String name;

  private String password;

  private String preferredColour;

  private Role role;

  /**
   * player info.
   */
  public Player() {

  }

  /**
   * Player.
   *
   * @param name            name
   * @param preferredColour preferredColour
   * @param password        password
   * @param role            role
   */
  public Player(String name, String preferredColour, String password, Role role) {
    this.name = name;
    this.preferredColour = preferredColour;
    this.password = password;
    this.role = role;
  }

  /**
   * getName.
   *
   * @return string
   */
  public String getName() {
    return name;
  }

  /**
   * setName.
   *
   * @param name name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * getPassword.
   *
   * @return String
   */
  public String getPassword() {
    return password;
  }

  /**
   * setPassword.
   *
   * @param password password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * getPreferredColour.
   *
   * @return String
   */
  public String getPreferredColour() {
    return preferredColour;
  }

  /**
   * setPreferredColour.
   *
   * @param preferredColour preferredColour
   */
  public void setPreferredColour(String preferredColour) {
    this.preferredColour = preferredColour;
  }

  /**
   * getRole.
   *
   * @return role
   */
  public Role getRole() {
    return role;
  }

  /**
   * setRole.
   *
   * @param role role
   */
  public void setRole(Role role) {
    this.role = role;
  }
}

