package main.java.ca.group8.gameservice.splendorgame.model;

import java.util.Optional;

public interface DevelopmentCard extends Card{
    int level = 0;
    Optional<Colour> GemColor = null;
    boolean isPaired = false;
    int pairedCardId = 0;

    public int getLevel();
    Optional<Colour> getGemColor();
    boolean isPaired();
    int getPairedCardID();
}
