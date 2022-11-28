package ca.group8.gameservice.splendorgame.controller.splendorlogic;
import ca.group8.gameservice.splendorgame.controller.GameRestController;
import ca.group8.gameservice.splendorgame.controller.SplendorRegistrator;
import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class SplendorRestController implements GameRestController {


  @Autowired
  private SplendorRegistrator splendorRegistrator;

  public SplendorRestController() {

  }
  @GetMapping("/hello")
  public String hello() {
    return "Hello";
  }

  @GetMapping("/test")
  public String test() {
    return "This is working!";
  }

  @Override
  public ResponseEntity<String> launchGame(long gameId, LauncherInfo launcherInfo) {
    return null;
  }

  @Override
  public ResponseEntity<String> deleteGame(long gameId) {
    return null;
  }

  @Override
  public DeferredResult<ResponseEntity<String>> getBoard(long gameId, String hash) {
    return null;
  }

  @Override
  public ResponseEntity<String> getPlayers(long gameId) {
    return null;
  }

  @Override
  public ResponseEntity<String> getActions(long gameId, String player, String accessToken) {
    return null;
  }

  @Override
  public ResponseEntity<String> selectAction(long gameId, String player, String actionMD5,
                                             String accessToken) {
    return null;
  }

  @Override
  public ResponseEntity<String> getRanking(long gameId) {
    return null;
  }
}
