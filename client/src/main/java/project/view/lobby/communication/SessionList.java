package project.view.lobby.communication;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Sessions for response.
 */
public class SessionList {

  private final Map<Long, Session> sessions;

  /**
   * SessionList.
   */
  public SessionList() {
    this.sessions = new HashMap<>();
  }

  /**
   * get Sessions.
   *
   * @return a map
   */
  public Map<Long, Session> getSessions() {
    return sessions;
  }

  /**
   * getSessionById.
   *
   * @param sessionId sessionId
   * @return string
   */
  public Session getSessionById(Long sessionId) {
    return sessions.get(sessionId);
  }

  public Set<Long> getSessionIds() {
    return sessions.keySet();
  }

  /**
   * getSessionsCount.
   *
   * @return int
   */
  public int getSessionsCount() {
    return sessions.size();
  }

}
