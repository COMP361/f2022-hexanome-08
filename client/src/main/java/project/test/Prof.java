package project.test;

public class Prof implements People{
  public Prof(String university, String classes) {
    this.university = university;
    this.classes = classes;
  }

  public String getUniversity() {
    return university;
  }

  public String getClasses() {
    return classes;
  }

  public void setUniversity(String university) {
    this.university = university;
  }

  public void setClasses(String classes) {
    this.classes = classes;
  }

  private String university;
  private String classes;


  @Override
  public String getName() {
    return null;
  }
}
