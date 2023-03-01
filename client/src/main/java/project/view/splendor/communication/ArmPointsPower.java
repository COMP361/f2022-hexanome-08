package project.view.splendor.communication;

public class ArmPointsPower extends Power {

    public ArmPointsPower() {
        super(false,PowerEffect.ARM_POINTS);
        super.type = this.getClass().getSimpleName();
    }
}
