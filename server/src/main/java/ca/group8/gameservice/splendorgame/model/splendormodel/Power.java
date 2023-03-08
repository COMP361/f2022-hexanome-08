package ca.group8.gameservice.splendorgame.model.splendormodel;

import io.github.isharipov.gson.adapters.JsonSubtype;
import io.github.isharipov.gson.adapters.JsonType;
import java.util.Objects;

/**
 * This class represents the Power functionality in the Trading Post Extension.
 * <p>
 * Every abstract class was serialized/deserialized using the repository from:
 * https://medium.com/@iliamsharipov_56660/handling-polymorphism-with-gson-f4a702014ffe.
 * Thank him so much!!!!!!!!!!!!!!!
 */
@JsonType(
    property = "type",
    subtypes = {
        @JsonSubtype(clazz = ArmPointsPower.class, name = "ArmPointsPower"),
        @JsonSubtype(clazz = TwoPlusOnePower.class, name = "TwoPlusOnePower"),
        @JsonSubtype(clazz = DoubleGoldPower.class, name = "DoubleGoldPower"),
        @JsonSubtype(clazz = ExtraTokenPower.class, name = "ExtraTokenPower"),
        @JsonSubtype(clazz = FivePointsPower.class, name = "FivePointsPower")
    }
)
public abstract class Power {
  private final PowerEffect powerEffect;
  String type;
  private boolean unlocked;

  /**
   * The constructs a new power object, which is tied to a specific Player.
   *
   * @param powerEffect An enum representing which power this object is.
   */
  public Power(PowerEffect powerEffect) {
    this.unlocked = false; //default value
    this.powerEffect = powerEffect;
  }

  /**
   * This method checks to see if a player meets the qualifications to unlock this power.
   *
   * @param playerInfo the Player whose qualifications we are checking.
   * @return a boolean value of whether the Player can unlock this power.
   */
  public abstract boolean validityCheck(PlayerInGame playerInfo);

  public boolean isUnlocked() {
    return unlocked;
  }

  //Do not make these methods [unlock() & lock()]public as they should only be called directly by
  // an inheriting power class (ex. TwoPlusOnePower).
  public void unlock() {
    unlocked = true;
  }

  public void lock() {
    unlocked = false;
  }

  public PowerEffect getPowerEffect() {
    return powerEffect;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Power)) {
      return false;
    }

    Power other = (Power) obj;
    return this.unlocked == other.unlocked &&
        this.powerEffect.equals(other.powerEffect);
  }

  @Override
  public int hashCode() {
    return Objects.hash(unlocked, powerEffect);
  }
}
