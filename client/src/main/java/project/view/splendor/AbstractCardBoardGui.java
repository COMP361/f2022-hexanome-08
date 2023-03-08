package project.view.splendor;

import ca.mcgill.comp361.splendormodel.actions.Action;
import ca.mcgill.comp361.splendormodel.actions.PurchaseAction;
import ca.mcgill.comp361.splendormodel.actions.ReserveAction;
import ca.mcgill.comp361.splendormodel.model.DevelopmentCard;
import ca.mcgill.comp361.splendormodel.model.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCardBoardGui {

  public Map<String, Action> filterOrientCardActions(Map<String, Action> rawActionMap) {
    Map<String, Action> result = new HashMap<>();
    for (String actionId : rawActionMap.keySet()) {
      Action curAction = rawActionMap.get(actionId);
      Position cardPosition = null;
      if (curAction instanceof PurchaseAction) {

      }

      if (curAction instanceof ReserveAction) {

      }

      if (cardPosition != null) {

      }
    }


  }


}
