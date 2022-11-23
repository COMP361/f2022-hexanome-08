package main.java.ca.group8.gameservice.splendorgame.model;

import java.util.EnumMap;

public interface Card {
    int cardId = 0;
    int prestigePoints = 0;
    EnumMap<Colour, Integer> price = null;
}
