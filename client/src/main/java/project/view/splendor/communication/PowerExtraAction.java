package project.view.splendor.communication;

public class PowerExtraAction extends Action{
    private final PowerEffect powerEffect;
    public PowerExtraAction(String type, PowerEffect powerEffect) {
        this.powerEffect = powerEffect;
    }

    public PowerEffect getPowerEffect() {
        return powerEffect;
    }

}
