package project.test;

import io.github.isharipov.gson.adapters.JsonSubtype;
import io.github.isharipov.gson.adapters.JsonType;

//@JsonType(
//    property = "type",
//    subtypes = {
//        @JsonSubtype(clazz = Student.class, name = "student"),
//        @JsonSubtype(clazz = Prof.class, name = "prof")
//    }
//)
public interface People {

  String getName();
}
