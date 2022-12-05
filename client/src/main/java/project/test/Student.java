package project.test;

import io.github.isharipov.gson.adapters.JsonSubtype;
import io.github.isharipov.gson.adapters.JsonType;
import project.view.splendor.communication.PurchaseAction;
import project.view.splendor.communication.ReserveAction;
import project.view.splendor.communication.TakeTokenAction;


@JsonType(
    property = "type",
    subtypes = {
        @JsonSubtype(clazz = Undergrad.class, name = "undergrad"),
        @JsonSubtype(clazz = Grad.class, name = "grad")
    }
)
public abstract class Student{
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  private String type;
  private String address;


  public String getAddress() {
    return address;
  }

  public Student(String address) {
    this.address = address;
  }


  public void setAddress(String address) {
    this.address = address;
  }

  public abstract String getName();
}
