package ca.group8.gameservice.splendorgame.model;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.Player;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

/**
 * General game manager entity that serves as entry point for access to the model. Adherent classes can be injected into
 * the contoller. On the long run there will by multiple implementations, depending on whether an in-RAM model
 * management or Database mangament (which allows for multiple instances of the service to be deployed simultaneously)
 * is desired.
 *
 * @Author: Maximilian Schiedermeier
 * @Date: December 2020
 */
//@Component
public interface GameManager {

    /**
     * Retrieves a specific game, by Id.
     *
     * @param gameId as the game to look up.
     * @return the referenced game, if found.
     */
    GameInfo getGameById(long gameId) throws ModelAccessException;

    /**
     * Tells whether a provided game id is known.
     *
     * @param gameId as the game to look up.
     * @return true if the provided id can be resolved to a game, false otherwise.
     */
    boolean isExistentGameId(long gameId);

    /**
     * Adds a new blank game to the manager
     *
     * @param gameId  id provided by the lobby-service
     * @param players as game participants in the order of joining
     * @return
     */
    GameInfo addGame(long gameId, ArrayList<String> playerNames)
        throws ModelAccessException, FileNotFoundException;

    /**
     * Removes an indexed game. The action is rejected adn a IllegalModelAccessException is thrown, if the game is not
     * yet finished, but the corresponding flag is set to false.
     *
     * @param gameId           as the game to by removed
     * @param evenIfUnfinished as a safety flag to prevent unitended deletion of running games
     * @throws ModelAccessException in case the game is still runnung by the previous parameter is set to false
     */
    void removeGame(long gameId) throws ModelAccessException;
}