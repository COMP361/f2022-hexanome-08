package project.view.splendor.communication;

public abstract class Power {
    String type;
    private boolean unlocked;

    private final PowerEffect powerEffect;

    public Power(boolean unlocked, PowerEffect powerEffect) {
        this.unlocked = unlocked;
        this.powerEffect = powerEffect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public PowerEffect getPowerEffect() {
        return powerEffect;
    }
}
