package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Noble Cards.
 */
public class NobleCard extends Card {

  public NobleCard(int paramPrestigePoints,
                   EnumMap<Colour, Integer> paramPrice, String paramCardName) {
    super(paramPrestigePoints, paramPrice, paramCardName);

  }

  /**
   * Check whether this noble card can be visited by this player or not.
   *
   * @param playerInGame player's info needed to check condition.
   * @return decision whether this noble can be visited or not
   */
  public boolean canVisit(PlayerInGame playerInGame) {
    // unlocking conditions: enough points + price
    EnumMap<Colour, Integer> curPrice = super.getPrice();
    EnumMap<Colour, Integer> playerDevCount = playerInGame.getTotalGems();
    // use player gem count - price to find diff of the prices
    Map<Colour, Integer> diffPrice = playerDevCount.keySet().stream()
        .filter(curPrice::containsKey)
        .collect(Collectors.toMap(
            key -> key,
            key -> playerDevCount.get(key) - curPrice.get(key)
        ));
    // if diff >= 0, then player has enough card to have this noble visit him/her
    return diffPrice.values().stream().allMatch(count -> count >= 0);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof Card)) {
      return false;
    }

    NobleCard other = (NobleCard) o;

    return super.equals(other);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }
}

