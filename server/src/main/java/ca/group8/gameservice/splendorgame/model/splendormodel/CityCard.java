package ca.group8.gameservice.splendorgame.model.splendormodel;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class that holds info about CityBoard.
 */
public class CityCard extends Card {

  private final int anyColourCount;

  /**
   * CityCard infos.
   *
   * @param paramPrestigePoints paramPrestigePoints
   * @param paramPrice paramPrice
   * @param paramCardName paramCardName
   * @param anyColourCount anyColourCount
   */
  public CityCard(int paramPrestigePoints, EnumMap<Colour, Integer> paramPrice,
                  String paramCardName, int anyColourCount) {
    super(paramPrestigePoints, paramPrice, paramCardName);
    super.type = this.getClass().getSimpleName();
    this.anyColourCount = anyColourCount;
  }

  public int getAnyColourCount() {
    return anyColourCount;
  }

  /**
   * Check whether this city card can be unlocked by this player or not.
   *
   * @param playerInGame player's info needed to check condition.
   * @return decision whether this city can be unlocked or not
   */
  public boolean canUnlock(PlayerInGame playerInGame) {
    // unlocking conditions: enough points + price
    EnumMap<Colour, Integer> curPrice = super.getPrice();
    EnumMap<Colour, Integer> playerDevCount = playerInGame.getTotalGems();
    // use player gem count - price to find diff
    Map<Colour, Integer> diffPrice = playerDevCount.keySet().stream()
        .filter(curPrice::containsKey)
        .collect(Collectors.toMap(
            key -> key,
            key -> playerDevCount.get(key) - curPrice.get(key)
        ));

    // if before counting the anyColourCount player cant afford it, no need to check other colours
    boolean enoughGems = diffPrice.values().stream().allMatch(count -> count >= 0);

    // needs to check any other colour of dev cards
    if (enoughGems && anyColourCount > 0) {
      // if among the remaining price, we still find player can afford it, allow this unlock
      enoughGems = diffPrice.values().stream().anyMatch(count -> count >= anyColourCount);
    }

    return enoughGems && super.getPrestigePoints() <= playerInGame.getPrestigePoints();

  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof Card)) {
      return false;
    }

    CityCard other = (CityCard) o;

    return super.equals(other) && this.anyColourCount == other.anyColourCount;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), anyColourCount);
  }


}
