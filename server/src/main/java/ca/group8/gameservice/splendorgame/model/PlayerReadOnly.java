package ca.group8.gameservice.splendorgame.model;

/**
 * Read only interface for properties of a player, as provided by the LobbyService.
 *
 * @Author: Maximilian Schiedermeier
 * @Date: December 2020
 */
public interface PlayerReadOnly {

    /**
     * Getter for the name of a player.
     *
     * @return
     */
    String getName();

    /**
     * Getter for the preferred colour of a player.
     *
     * @return
     */
    String getPreferredColour();

    /**
     * Equals comparison. Only matches names. Ignores upper/lower cases.
     *
     * @param other as the player object to compare with.
     * @return a boolean that indicates whether the names of the provided player objects match.
     */
    boolean equals(Object other);
}
