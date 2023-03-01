package project.view.splendor.communication;

public class CardExtraAction extends Action {
    private Card curCard;
    //TODO: Check this works after code is merged
    private final CardEffect cardEffect;

    private final Position position;


    public CardExtraAction(String type, Card curCard, CardEffect cardEffect, project.view.splendor.communication.Position position) {
        this.curCard = curCard;
        this.cardEffect = cardEffect;
        this.position = position;
    }

    public Card getCurCard() {
        return curCard;
    }

    public void setCurCard(Card curCard) {
        this.curCard = curCard;
    }

    public CardEffect getCardEffect() {
        return cardEffect;
    }

    public project.view.splendor.communication.Position getPosition() {
        return position;
    }
}
