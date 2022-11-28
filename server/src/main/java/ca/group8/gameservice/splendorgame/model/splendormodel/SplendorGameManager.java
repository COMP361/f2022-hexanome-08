package ca.group8.gameservice.splendorgame.model.splendormodel;
import ca.group8.gameservice.splendorgame.model.ModelAccessException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SplendorGameManager{
    private Map<Long, GameInfo> activeGames;


    public SplendorGameManager() {
        this.activeGames = new HashMap<>();
    }

    public GameInfo getGameById(long gameId) throws ModelAccessException {
        if (!isExistentGameId(gameId)){
            throw new ModelAccessException("No registered game with that ID");
        }
        return activeGames.get(gameId);
    }


    public boolean isExistentGameId(long gameId) {
        return activeGames.containsKey(gameId);
    }

    public GameInfo addGame(long gameId, ArrayList<String> playerNames)
        throws ModelAccessException, FileNotFoundException {
        GameInfo newGameInfo = new GameInfo(playerNames);
        activeGames.put(gameId,newGameInfo);
        return newGameInfo;
    }

    public void removeGame(long gameId) {
        activeGames.remove(gameId);
    }



    public Map<Long, GameInfo> getActiveGames() {
        return activeGames;
    }



}
