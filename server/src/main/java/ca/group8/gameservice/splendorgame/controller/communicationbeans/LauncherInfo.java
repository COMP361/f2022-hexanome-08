package ca.group8.gameservice.splendorgame.controller.communicationbeans;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The class needed to encode/decode to JSON String as the information to communicate with LS.
 *
 */
public class LauncherInfo {

    // The name of the game that registered in LS
    String gameServer;

    LinkedList<PlayerInfo> players;

    String creator;

    // Optional id of a saved game to load, it will be a request param from client request
    String savegame;

    public LauncherInfo() {
    }

    // by not providing the save game id, we create a new game
    public LauncherInfo(String gameServer, LinkedList<PlayerInfo> players, String creator) {
        this.gameServer = gameServer;
        this.players = players;
        this.creator = creator;
        savegame = "";
    }

    // by providing the save game id, we create a game based a previous saved game states
    public LauncherInfo(String gameServer, LinkedList<PlayerInfo> players, String creator, String savegame) {
        this.gameServer = gameServer;
        this.players = players;
        this.creator = creator;
        this.savegame = savegame;
    }

    public String getGameServer() {
        return gameServer;
    }

    public LinkedList<PlayerInfo> getPlayers() {
        return players;
    }

    public String getCreator() {
        return creator;
    }

    public String getSavegame() {
        return savegame;
    }

    public void setGameServer(String gameServer) {
        this.gameServer = gameServer;
    }

    public void setPlayers(LinkedList<PlayerInfo> players) {
        this.players = players;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setSavegame(String savegame) {
        this.savegame = savegame;
    }

}
