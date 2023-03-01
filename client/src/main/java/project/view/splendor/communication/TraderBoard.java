package project.view.splendor.communication;

import java.util.HashMap;
import java.util.Map;

public class TraderBoard extends Board {
    private Map<String, Map<PowerEffect, Power>> allPlayerPowers = new HashMap<>();

    public TraderBoard(String type, Map<String, Map<PowerEffect, Power>> allPlayerPowers) {
        super(type);
        this.allPlayerPowers = allPlayerPowers;
    }

    public Map<String, Map<PowerEffect, Power>> getAllPlayerPowers() {
        return allPlayerPowers;
    }

    public void setAllPlayerPowers(Map<String, Map<PowerEffect, Power>> allPlayerPowers) {
        this.allPlayerPowers = allPlayerPowers;
    }
}
