package ca.group8.gameservice.splendorgame.model;

import ca.group8.gameservice.splendorgame.model.splendormodel.Board;
import ca.group8.gameservice.splendorgame.model.splendormodel.TableTop;

/**
 * Generic game interface with methods common to all board game implementations. The Game represents a persistable game
 * state allowing full game replay. This includes a reference to a game Board as well as e.g. gameplay history snapshots
 * or random generator seed values.
 *
 * @Author: Maximilian Schiedermeier
 * @Date: December 2020
 */
public interface Game {

    /**
     * Look up a player by name
     *
     * @param name as the name of the player to look up.b
     * @return PlayerReadOnly as the matching player object, if associated. Throws a ModelAccessException if the
     * provided player is not associated to this game object.
     */
    PlayerReadOnly getPlayerByName(String name) throws ModelAccessException;

    /**
     * Retrieves the board of a generic game.
     *
     * @return the board of a game.
     */
    TableTop getTableTop();

    /**
     * Retrieves the array (in order of registration) of players associated to a game object. Only returns a deep copy
     * of the original players array.
     *
     * @return a deep copy of the player array associated to a game.
     */
    PlayerReadOnly[] getPlayers();

    /**
     * Flag to indicate whether a game instance is not modifiable any more.
     *
     * @return true if no more moves are possible. False otherwise.
     */
    boolean isFinished();
}
