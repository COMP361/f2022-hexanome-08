package project.test;

public abstract class Student implements People{
  public String getAddress() {
    return address;
  }

  public Student(String address) {
    this.address = address;
  }

  private String address;

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String getName() {
    return null;
  }
}
