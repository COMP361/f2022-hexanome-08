package project.view.splendor.communication;

public class ClaimNobleAction extends Action{
    private DevelopmentCard curCard;
    private Position curPosition;

    public ClaimNobleAction(String type, DevelopmentCard curCard, Position curPosition) {
        this.curCard = curCard;
        this.curPosition = curPosition;
    }

    public DevelopmentCard getCurCard() {
        return curCard;
    }

    public void setCurCard(DevelopmentCard curCard) {
        this.curCard = curCard;
    }

    public Position getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(Position curPosition) {
        this.curPosition = curPosition;
    }



}
