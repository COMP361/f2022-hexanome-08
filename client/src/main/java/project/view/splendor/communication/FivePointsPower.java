package project.view.splendor.communication;

public class FivePointsPower extends Power {

    public FivePointsPower() {
        super(false, PowerEffect.FIVE_POINTS);
        super.type = this.getClass().getSimpleName();
    }
}
