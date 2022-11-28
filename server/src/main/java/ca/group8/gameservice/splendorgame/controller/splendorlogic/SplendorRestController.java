package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.SplendorRegistrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SplendorRestController {

  @Autowired
  private SplendorRegistrator splendorRegistrator;
}
