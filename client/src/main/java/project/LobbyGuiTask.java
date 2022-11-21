package project;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.Map;
import java.util.Set;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import project.App;
import project.LobbyController;
import project.connection.LobbyServiceRequestSender;
import project.view.lobby.Session;
import project.view.lobby.User;

/**
 * Lobby Gui update task class.
 */
public class LobbyGuiTask extends Task<Void> {


  private final LobbyServiceRequestSender lobbyRequestSender;
  private final VBox sessionVbox;
  private final LobbyController lobbyController;


  /**
   * Constructor of LobbyGuiTask.
   *
   * @param lsSender     LS request sender
   * @param psessionVbox where sessions are updated visually
   * @param lc           lobby controller
   */
  public LobbyGuiTask(LobbyServiceRequestSender lsSender, VBox psessionVbox, LobbyController lc) {
    lobbyRequestSender = lsSender;
    sessionVbox = psessionVbox;
    lobbyController = lc;
  }


  @Override
  protected Void call() throws Exception {
    System.out.println("Trying to start gui update task in:" + Thread.currentThread().getName());
    // Start busy waiting by trying to update with the latest session info
    Map<String, Session> remoteSessionIdMap;
    Map<String, Session> localSessionIdMap = lobbyRequestSender.getSessionIdMap();
    try {
      remoteSessionIdMap = lobbyRequestSender.getRemoteSessions();
    } catch (UnirestException e) {
      throw new RuntimeException(e);
    }
    if (remoteSessionIdMap.isEmpty()) {
      return null;
    }
    // if we have any remote sessions, then we need to sync local session ids
    // with the remote ones (either add or remove session from local sessionIdMap)
    User user = App.getUser();
    if (localSessionIdMap.isEmpty() && user != null) {
      // local has no record of any sessions, add all default ones
      // then copy the whole thing
      lobbyRequestSender.setSessionIdMap(remoteSessionIdMap);
      // generate all GUI if user logged in
      String accessToken = user.getAccessToken();
      localSessionIdMap = lobbyRequestSender.getSessionIdMap();
      lobbyController.addSessionsGui(localSessionIdMap, accessToken, sessionVbox);
    } else { // localSessionIdMap is not empty because it can only be
      if (user != null) { // stop client from updating if user log out
        int remoteSessionCount = remoteSessionIdMap.size();
        int localSessionCount = localSessionIdMap.size();
        // local already has a record of some sessions
        Set<String> remoteSessionIds = remoteSessionIdMap.keySet();
        Set<String> localSessionIds = localSessionIdMap.keySet();

        // TODO: Shallow copy might be a future problem....
        if (remoteSessionCount > localSessionCount) {
          // remote has more sessions, need to create ones locally
          remoteSessionIds.removeAll(localSessionIds);
          for (String sessionId : remoteSessionIds) {
            Session remoteSession = remoteSessionIdMap.get(sessionId);
            Session newLocalSession = new Session(remoteSession);
            // added new session to local session id map
            localSessionIdMap.put(sessionId, newLocalSession);
          }
          // update local session id map
          lobbyRequestSender.setSessionIdMap(localSessionIdMap);
          // GUI: add the new sessions
          // generate all GUI if user logged in
          String accessToken = user.getAccessToken();
          lobbyController.addSessionsGui(localSessionIdMap, accessToken, sessionVbox);

        } else if (localSessionCount > remoteSessionCount) {
          // local has more sessions, need to delete ones locally
          localSessionIds.removeAll(remoteSessionIds);
          // update local session id map (if user logged in)
          lobbyRequestSender.setSessionIdMap(localSessionIdMap);
          lobbyController.removeSessionsGui(localSessionIdMap, localSessionIds, sessionVbox);
        }

        // TODO: After having the updated local sessions id map, we can update GUI
        // proceed with updating all local session ids' session info
        // in the case of localSessionCount == remoteSessionCount
        // local session map will not be updated, we manually update it here
        System.out.println(localSessionIdMap.keySet());
        localSessionIdMap = lobbyRequestSender.getSessionIdMap();
        for (Node n : sessionVbox.getChildren()) {
          lobbyController.updateSessionsGui(localSessionIdMap, n);
        }
      }

    }
    return null;
  }
}

