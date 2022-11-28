package ca.group8.gameservice.splendorgame.model.splendormodel;

import ca.group8.gameservice.splendorgame.model.ModelAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplendorGameManager {
    private Map<Long, GameInfo> activeGames = new HashMap<>();


    public GameInfo getGameById(long gameId) throws ModelAccessException {
        if (!isExistentGameId(gameId)){
            throw new ModelAccessException("No registered game with that ID");
        }
        return activeGames.get(gameId);
    }

    public boolean isExistentGameId(long gameId) {
        if (activeGames.containsKey(gameId)) {
            return true;
        }
        return false;
    }


    public void addGame(long gameId, ArrayList<String> playerNames) {
        activeGames.put(gameId, new GameInfo(playerNames));

    }


    public void removeGame(long gameId) {
        activeGames.remove(gameId);
    }
}
