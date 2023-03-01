package project.view.splendor.communication;

public class TwoPlusOnePower extends Power {

    public TwoPlusOnePower() {
        super(false, PowerEffect.TWO_PLUS_ONE);
        super.type = this.getClass().getSimpleName();
    }
}
