package project.view.splendor.communication;

public class ExtraTokenPower extends Power {

    public ExtraTokenPower() {
        super(false,PowerEffect.EXTRA_TOKEN);
        super.type = this.getClass().getSimpleName();
    }
}
