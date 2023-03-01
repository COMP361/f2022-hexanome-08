package project.view.splendor.communication;

public class DoubleGoldPower extends Power {

    public DoubleGoldPower() {
        super(false,PowerEffect.DOUBLE_GOLD);
        super.type = this.getClass().getSimpleName();
    }
}
