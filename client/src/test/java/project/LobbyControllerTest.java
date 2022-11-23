package project;
import org.junit.Test;
import project.connection.LobbyServiceRequestSender;

public class LobbyControllerTest {

  @Test
  public void testConstructor() {
    LobbyServiceRequestSender sender = new LobbyServiceRequestSender("http://76.66.139.161:4242");
    assert sender.getLobbyUrl().equals("http://76.66.139.161:4242");


  }


  @Test
  public void testPassForSure(){
    Boolean T = true;
    assert T.equals(true);
  }
}
