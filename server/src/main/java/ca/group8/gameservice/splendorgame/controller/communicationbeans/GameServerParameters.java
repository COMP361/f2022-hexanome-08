package ca.group8.gameservice.splendorgame.controller.communicationbeans;


/**
 * Communication bean class needed for game server to talk to LS. (parsing / unparsing JSON)
 *
 */
public class GameServerParameters {
    private String name;

    private String displayName;

    private String location;

    private int maxSessionPlayers;
    private int minSessionPlayers;

    private String webSupport;

    public GameServerParameters(String name, String displayName, String location,
                                int maxSessionPlayers,
                                int minSessionPlayers, String webSupport) {
        this.name = name;
        this.displayName = displayName;
        this.location = location;
        this.maxSessionPlayers = maxSessionPlayers;
        this.minSessionPlayers = minSessionPlayers;
        this.webSupport = webSupport;
    }


    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLocation() {
        return location;
    }

    public int getMaxSessionPlayers() {
        return maxSessionPlayers;
    }

    public int getMinSessionPlayers() {
        return minSessionPlayers;
    }

    public String getWebSupport() {
        return webSupport;
    }




}
