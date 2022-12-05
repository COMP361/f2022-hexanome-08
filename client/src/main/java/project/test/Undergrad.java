package project.test;

public class Undergrad extends Student{
  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  private int year;
  public Undergrad(String address, int year) {
    super(address);
    this.year = year;
  }

  @Override
  public String getName() {
    return null;
  }
}
