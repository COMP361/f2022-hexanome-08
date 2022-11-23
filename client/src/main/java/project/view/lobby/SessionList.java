package project.view.lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Sessions for response.
 */
public class SessionList {

  private final Map<Long, Session> sessionIdMap;

  public SessionList() {
    this.sessionIdMap = new HashMap<>();
  }

  public Map<Long, Session> getSessionIdMap() {
    return sessionIdMap;
  }

  public Session getSessionById (Long sessionId) {
    return sessionIdMap.get(sessionId);
  }

  public Set<Long> getSessionIds() {
    return sessionIdMap.keySet();
  }

  public int getSessionsCount() {
    return sessionIdMap.size();
  }

}
