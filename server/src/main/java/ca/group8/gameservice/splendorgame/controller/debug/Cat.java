package ca.group8.gameservice.splendorgame.controller.debug;

public class Cat extends Animal {
  int numLives;

  public Cat(String name, int numLives) {
    super.type = "cat";
    this.name = name;
    this.numLives = numLives;
  }
}