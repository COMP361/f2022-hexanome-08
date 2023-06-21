package project.config;

/**
 * ConnectionConfig.
 */
public class UserConfig {
  private String defaultUserName;
  private String defaultPassword;
  private boolean useDefaultUserInfo;
  private String hostIp;

  private boolean useLocalHost;
  private double musicVolume;

  /**
   * ConnectionConfig.
   *
   * @param defaultUserName    defaultUserName
   * @param defaultPassword    defaultPassword
   * @param useDefaultUserInfo useDefaultUserInfo
   * @param hostIp             hostIp
   * @param useLocalHost       useLocalHost
   * @param musicVolume
   */
  public UserConfig(String defaultUserName, String defaultPassword,
                    boolean useDefaultUserInfo,
                    String hostIp, boolean useLocalHost, double musicVolume) {
    this.defaultUserName = defaultUserName;
    this.defaultPassword = defaultPassword;
    this.useDefaultUserInfo = useDefaultUserInfo;
    this.hostIp = hostIp;
    this.useLocalHost = useLocalHost;
    this.musicVolume = musicVolume;
  }

  /**
   * Getter method.
   *
   * @return default user name
   */
  public String getDefaultUserName() {
    return defaultUserName;
  }


  public boolean isUseLocalHost() {
    return useLocalHost;
  }

  public double getMusicVolume() {
    return musicVolume;
  }

  public void setMusicVolume(double musicVolume) {
    this.musicVolume = musicVolume;
  }


  /**
   * Getter method.
   *
   * @return default password
   */
  public String getDefaultPassword() {
    return defaultPassword;
  }

  /**
   * Getter method for boolean value.
   *
   * @return true if the default user info should be used
   */
  public boolean isUseDefaultUserInfo() {
    return useDefaultUserInfo;
  }

  /**
   * gotHostIp.
   *
   * @return string
   */
  public String getHostIp() {
    if (useLocalHost) {
      return "127.0.0.1";
    }
    return hostIp;
  }

  public void setDefaultUserName(String defaultUserName) {
    this.defaultUserName = defaultUserName;
  }

  public void setDefaultPassword(String defaultPassword) {
    this.defaultPassword = defaultPassword;
  }

  public void setUseDefaultUserInfo(boolean useDefaultUserInfo) {
    this.useDefaultUserInfo = useDefaultUserInfo;
  }

  public void setHostIp(String hostIp) {
    this.hostIp = hostIp;
  }

  public void setUseLocalHost(boolean useLocalHost) {
    this.useLocalHost = useLocalHost;
  }

}
