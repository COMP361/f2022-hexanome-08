package ca.group8.gameservice.splendorgame.controller.debug;

import io.github.isharipov.gson.adapters.JsonSubtype;
import io.github.isharipov.gson.adapters.JsonType;

@JsonType(
    property = "type",
    subtypes = {
        @JsonSubtype(clazz = Dog.class, name = "dog"),
        @JsonSubtype(clazz = Cat.class, name = "cat")
    }
)
public abstract class Animal {
  String name;
  String type;

}