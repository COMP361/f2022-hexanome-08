package ca.group8.gameservice.splendorgame.controller;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.LauncherInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Generic interface for REST endpoint methods common to all board game implementations. All methods require the
 * specification of a specific game-entity, identified by the long-id provided by the lobby-service upon game creation.
 * Note that non-void return types of response entitied are always Strings. This allows for a JSON / XML encoding of the
 * result objects.
 *
 * @Author: Maximilian Schiedermeier
 * @Date: December 2020
 */
public interface GameRestController {

    /**
     * Creates a new game, identified by the provided unique long value.
     *
     * @param gameId       as the game key.
     * @param launcherInfo as additional parameters for the game to be created
     */
    ResponseEntity<String> launchGame(long gameId, LauncherInfo launcherInfo);

    /**
     * Deletes a new game, identified by the provided unique long value. This endpoint is commonly not exposed by the
     * API gateway (only accessible by lobby service.). Therefore no token is required to authorize this operation.
     *
     * @param gameId      as the game key.
     */
    ResponseEntity<String> deleteGame(long gameId);

    /**
     * Getter for the game board. This end point should be refreshed regularly, to allow for asynchronous client
     * updates. The hash value can be set to the empty string to ignore long-polling.
     *
     * @param gameId as the game key.
     * @param hash   as the optional MD5 hash string of the most recent board state on client side.
     * @return
     */
    DeferredResult<ResponseEntity<String>> getBoard(long gameId, String hash);

    /**
     * Getter for static player objects (names, preferred colours) of the participants of the game instance referenced
     * by the provided game-id.
     *
     * @param gameId as the game key.
     * @return
     */
    ResponseEntity<String> getPlayers(long gameId);

    /**
     * Method to look up possible actions for a given player and session. This end point should be access protected, for
     * it potentially discloses information restricted to the player oin question. Note that there is no
     * generic/prepared method to handle user-selected actions. Following the REST approach, actions might directly
     * modify game/board-specific sub-resources which can not be assumed in a generic interface.
     *
     * @param gameId      as the key to resolve the referenced game-instance
     * @param player      as the player requesting a set of available actions
     * @param accessToken as the OAuth2 Access token used to authorize this operation
     */
    ResponseEntity<String> getActions(long gameId, String player, String accessToken);

    /**
     * Blackboard-style way to allow a client select a specific operation. The operation is identified by the MD5
     * representation of it's JSON serialization.
     *
     * @param gameId      as the key to resolve the referenced game-instance
     * @param player      as the player requesting to play an action
     * @param actionMD5   as the identifier of the selected action
     * @param accessToken as the
     */
    ResponseEntity<String> selectAction(long gameId, String player, String actionMD5, String accessToken);

    /**
     * Returns current player scores as a serialized ranking object.
     *
     * @param gameId
     */
    ResponseEntity<String> getRanking(long gameId);

    /**
     * Returns information about the lobbyservice location.
     */

}
