package ca.group8.gameservice.splendorgame.controller.debug;

public class Dog extends Animal {
  String breed;
  int age;

  public Dog(String name, String breed, int age) {
    super.type = "dog";
    this.name = name;
    this.breed = breed;
    this.age = age;
  }
}