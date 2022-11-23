package project.view.lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Sessions for response.
 */
public class SessionList {

  private final Map<Long, Session> sessions;

  public SessionList() {
    this.sessions = new HashMap<>();
  }

  public Map<Long, Session> getSessions() {
    return sessions;
  }

  public Session getSessionById(Long sessionId) {
    return sessions.get(sessionId);
  }

  public Set<Long> getSessionIds() {
    return sessions.keySet();
  }

  public int getSessionsCount() {
    return sessions.size();
  }

}
