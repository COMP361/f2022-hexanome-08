package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;
import io.github.isharipov.gson.adapters.JsonSubtype;
import io.github.isharipov.gson.adapters.JsonType;


/**
 * Generic Action abstract class for blackboard architecture.
 * Implement your own actions, representing kinds of actions
 * performed by players in a blackboard architecture. Actions are
 * generated by a custom ActionGenerator and performed by
 * a custom ActionInterpreter.
 * <p>
 * Every abstract class was serialized/deserialized using the repository from:
 * * https://medium.com/@iliamsharipov_56660/handling-polymorphism-with-gson-f4a702014ffe.
 * * Thank him so much!!!!!!!!!!!!!!!
 */


@JsonType(
    property = "type",
    subtypes = {
        @JsonSubtype(clazz = PurchaseAction.class, name = "PurchaseAction"),
        @JsonSubtype(clazz = CardExtraAction.class, name = "CardExtraAction"),
        @JsonSubtype(clazz = ClaimNobleAction.class, name = "ClaimNobleAction"),
        @JsonSubtype(clazz = PowerExtraAction.class, name = "PowerExtraAction"),
        @JsonSubtype(clazz = ReserveAction.class, name = "ReserveAction"),
        @JsonSubtype(clazz = TakeTokenAction.class, name = "TakeTokenAction"),
        @JsonSubtype(clazz = ReturnTokenAction.class, name = "ReturnTokenAction"),

    }
)
public abstract class Action {

  String type;

  abstract void execute(TableTop curTableTop, PlayerInGame playerInGame,
                        ActionGenerator actionListGenerator,
                        ActionInterpreter actionInterpreter);

  boolean checkIsExtraAction() {
    return false;
  }

  boolean checkIsCardAction() {
    return false;
  }

  abstract Card getCurCard() throws NullPointerException;

  abstract Position getCardPosition() throws NullPointerException;
}
