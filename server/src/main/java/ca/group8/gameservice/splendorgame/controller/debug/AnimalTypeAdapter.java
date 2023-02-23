package ca.group8.gameservice.splendorgame.controller.debug;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class AnimalTypeAdapter extends TypeAdapter<Animal> {

  private static final String TYPE_FIELD_NAME = "type";

  @Override
  public void write(JsonWriter out, Animal animal) throws IOException {
    out.beginObject();
    out.name(TYPE_FIELD_NAME).value(getTypeFieldName(animal));
    out.name("name").value(animal.name);
    if (animal instanceof Dog) {
      Dog dog = (Dog) animal;
      out.name("breed").value(dog.breed);
      out.name("age").value(dog.age);
    } else if (animal instanceof Cat) {
      Cat cat = (Cat) animal;
      out.name("numLives").value(cat.numLives);
    }
    out.endObject();
  }

  @Override
  public Animal read(JsonReader in) throws IOException {
    in.beginObject();
    String type = null;
    String name = null;
    String breed = null;
    int age = 0;
    int numLives = 0;
    while (in.hasNext()) {
      String fieldName = in.nextName();
      if (fieldName.equals(TYPE_FIELD_NAME)) {
        type = in.nextString();
      } else if (fieldName.equals("name")) {
        name = in.nextString();
      } else if (type != null && type.equals("dog") && fieldName.equals("breed")) {
        breed = in.nextString();
      } else if (type != null && type.equals("dog") && fieldName.equals("age")) {
        age = in.nextInt();
      } else if (type != null && type.equals("cat") && fieldName.equals("numLives")) {
        numLives = in.nextInt();
      } else {
        in.skipValue();
      }
    }
    in.endObject();
    if (type == null) {
      throw new JsonParseException("Missing type field");
    } else if (type.equals("dog")) {
      return new Dog(name, breed, age);
    } else if (type.equals("cat")) {
      return new Cat(name, numLives);
    } else {
      throw new JsonParseException("Unknown type: " + type);
    }
  }

  private String getTypeFieldName(Animal animal) {
    if (animal instanceof Dog) {
      return "dog";
    } else if (animal instanceof Cat) {
      return "cat";
    } else {
      throw new IllegalArgumentException("Unknown animal type: " + animal.getClass().getName());
    }
  }
}
