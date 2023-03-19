package project.view.lobby.communication;


/**
 * Communication class needed to update player information to LS.
 */
public class Player {

  private String name;

  private String password;

  private String preferredColour;

  private Role role;

  public Player() {

  }

  public Player(String name, String preferredColour, String password, Role role) {
    this.name = name;
    this.preferredColour = preferredColour;
    this.password = password;
    this.role = role;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPreferredColour() {
    return preferredColour;
  }

  public void setPreferredColour(String preferredColour) {
    this.preferredColour = preferredColour;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}

