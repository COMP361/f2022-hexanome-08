package project.view.lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Sessions for response.
 */
public class SessionList {

  private Map<String, Session> sesionIdMap;

  public SessionList() {
    this.sesionIdMap = new HashMap<>();
  }

  public Map<String, Session> getSesionIdMap() {
    return sesionIdMap;
  }

  public Session getSessionById (String sessionId) {
    return sesionIdMap.get(sessionId);
  }

  public Set<String> getSessionIds() {
    return sesionIdMap.keySet();
  }

  public void setSesionIdMap(SessionList newSessionList) {
    if (!sesionIdMap.isEmpty()) {
      sesionIdMap.clear();
    }
    sesionIdMap.putAll(newSessionList.getSesionIdMap());
  }

  public int getSessionsCount() {
    return sesionIdMap.size();
  }

}
