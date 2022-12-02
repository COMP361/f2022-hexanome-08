//package project.view.splendor.communication;
//
//import io.github.isharipov.gson.adapters.JsonSubtype;
//import io.github.isharipov.gson.adapters.JsonType;
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
//
//}
