package project.config;

public class ConnectionConfig {
  private final String defaultUserName;
  private final String defaultPassword;
  private final boolean useDefaultUserInfo;
  private final String hostIp;
  private final boolean useLocalHost;


  public ConnectionConfig(String defaultUserName, String defaultPassword,
                          boolean useDefaultUserInfo,
                          String hostIp, boolean useLocalHost) {
    this.defaultUserName = defaultUserName;
    this.defaultPassword = defaultPassword;
    this.useDefaultUserInfo = useDefaultUserInfo;
    this.hostIp = hostIp;
    this.useLocalHost = useLocalHost;
  }

  public String getDefaultUserName() {
    return defaultUserName;
  }

  public String getDefaultPassword() {
    return defaultPassword;
  }

  public boolean isUseDefaultUserInfo() {
    return useDefaultUserInfo;
  }

  public String getHostIp() {
    if (useLocalHost) {
      return "127.0.0.1";
    }
    return hostIp;
  }

}
