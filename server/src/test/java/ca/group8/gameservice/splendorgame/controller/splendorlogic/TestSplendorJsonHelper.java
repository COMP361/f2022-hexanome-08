package ca.group8.gameservice.splendorgame.controller.splendorlogic;

import ca.group8.gameservice.splendorgame.controller.communicationbeans.SplendorJsonHelper;
import ca.group8.gameservice.splendorgame.controller.debug.Animal;
import ca.group8.gameservice.splendorgame.controller.debug.AnimalTypeAdapter;
import ca.group8.gameservice.splendorgame.controller.debug.Cat;
import ca.group8.gameservice.splendorgame.controller.debug.Dog;
import ca.group8.gameservice.splendorgame.model.splendormodel.Board;
import ca.group8.gameservice.splendorgame.model.splendormodel.Colour;
import ca.group8.gameservice.splendorgame.model.splendormodel.Extension;
import ca.group8.gameservice.splendorgame.model.splendormodel.GameInfo;
import ca.group8.gameservice.splendorgame.model.splendormodel.PlayerStates;
import ca.group8.gameservice.splendorgame.model.splendormodel.Position;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.isharipov.gson.adapters.PolymorphDeserializer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ca.group8.gameservice.splendorgame.controller.RuntimeTypeAdapterFactory;
public class TestClassesSerialization {
  List<String> names = new ArrayList<>();
  List<Extension> extensions = new ArrayList<>();
  PlayerStates playerStates;

  @BeforeEach
  void setup() {
    names.add("Bob");
    names.add("Mary");
    extensions.add(Extension.BASE);
    playerStates = new PlayerStates(names);
  }

  @Test
  void testPlayerStates() {
    PlayerStates ps = new PlayerStates(names);
    String playerString = new Gson().toJson(ps, PlayerStates.class);
    PlayerStates ps2 = new Gson().fromJson(playerString, PlayerStates.class);
  }



  @Test
  void testGameInfo() {
    PlayerStates ps = new PlayerStates(names);
    GameInfo gameInfo = new GameInfo(extensions, names);
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Action.class, new PolymorphDeserializer<Action>())
        .registerTypeAdapter(Board.class, new PolymorphDeserializer<Board>())
        .create();
    String str = gson.toJson(gameInfo, GameInfo.class);
    GameInfo g = gson.fromJson(str, GameInfo.class);
  }

  @Test
  void testActionInterpreter() {
    PlayerStates ps = new PlayerStates(names);
    GameInfo gameInfo = new GameInfo(extensions, names);
    ActionInterpreter action = new ActionInterpreter(gameInfo, ps);
    Gson gson = SplendorJsonHelper.getInstance().getGson();
    String str = gson.toJson(action, ActionInterpreter.class);
    ActionInterpreter act2 = gson.fromJson(str, ActionInterpreter.class);
  }

  @Test
  void testAction() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Action.class, new PolymorphDeserializer<Action>())
        .create();
    Action a = new TakeTokenAction(new EnumMap<Colour, Integer>(Colour.class));
    String actionString = gson.toJson(a);
    Action a2 = gson.fromJson(actionString, Action.class);
  }

  @Test
  void testAnimal() {

    //RuntimeTypeAdapterFactory<Animal> animalAdapterFactory = RuntimeTypeAdapterFactory
    //    .of(Animal.class, "type")
    //    .registerSubtype(Dog.class, "dog")
    //    .registerSubtype(Cat.class, "cat");
    //Gson gson = new GsonBuilder()
    //    .registerTypeAdapter(Animal.class, new AnimalTypeAdapter())
    //    .create();

    //Gson gson = new GsonBuilder()
    //    .registerTypeAdapterFactory(animalAdapterFactory)
    //    .create();


    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Animal.class, new PolymorphDeserializer<Animal>())
        .create();
    Dog dog = new Dog("Buddy", "Golden Retriever", 3);
    Cat cat = new Cat("Whiskers", 9);

    String dogJson = gson.toJson(dog);
    String catJson = gson.toJson(cat);

    Animal dogDeserialized = gson.fromJson(dogJson, Animal.class);
    Animal catDeserialized = gson.fromJson(catJson, Animal.class);
  }
}
