//package ca.group8.gameservice.splendorgame.controller.splendorlogic;
//
//import ca.group8.gameservice.splendorgame.model.splendormodel.Card;
//import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
//import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerInGame;
//import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
//import io.github.isharipov.gson.adapters.JsonSubtype;
//import io.github.isharipov.gson.adapters.JsonType;
//
//
///**
// * Includes purchase and reserve actions.
// */
//
//@JsonType(
//    property = "type",
//    subtypes = {
//        @JsonSubtype(clazz = PurchaseAction.class, name = "purchaseAction"),
//        @JsonSubtype(clazz = ReserveAction.class, name = "reserveAction")
//    }
//)
//public abstract class CardAction extends Action {
//
//
//  private Position position;
//  private Card card;
//
//  public String getType() {
//    return type;
//  }
//
//  public void setType(String type) {
//    this.type = type;
//  }
//
//  private String type;
//
//  /**
//   * Constructor.
//   */
//  public CardAction(boolean isCardAction, Position position, Card card) {
//    super(isCardAction);
//    this.position = position;
//    this.card = card;
//  }
//
//  public Position getPosition() {
//    return position;
//  }
//
//  public Card getCard() {
//    return card;
//  }
//
//  public void setPosition(Position position) {
//    this.position = position;
//  }
//
//  public void setCard(Card card) {
//    this.card = card;
//  }
//
//  @Override
//  public abstract void execute(GameInfo currentGameState, PlayerInGame playerState);
//
//  //@Override
//  //public boolean checkIsCardAction() {
//  //  return true;
//  //}
//  //// Overriding equals() to compare two Complex objects
//  //
//  //// TODO: Override the equals for Card (do we need the equal for Card?)
//  //@Override
//  //public boolean equals(Object o) {
//  //
//  //  // If the object is compared with itself then return true
//  //  if (o == this) {
//  //    return true;
//  //  }
//  //
//  //      /* Check if o is an instance of Complex or not
//  //        "null instanceof [type]" also returns false */
//  //  if (!(this.getClass()==o.getClass())) {
//  //    return false;
//  //  }
//  //
//  //  // typecast o to CardAction so that we can compare data members
//  //  CardAction c = (CardAction) o;
//  //
//  //  // Compare the Player, Game, and Card fields
//  //  return this.getCard().equals(c.getCard());
//  //}
//
//}
