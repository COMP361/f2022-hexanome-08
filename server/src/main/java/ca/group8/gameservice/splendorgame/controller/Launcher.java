package ca.group8.gameservice.splendorgame.controller;


import ca.group8.gameservice.splendorgame.model.splendormodel.AvailableCards;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//@SpringBootApplication(scanBasePackages = {"ca.group8.gameservice.splendorgame"})
public class Launcher extends SpringBootServletInitializer {

  public static void main(String[] args) throws FileNotFoundException {
    File file = new File(
        Objects.requireNonNull(Launcher.class.getClassLoader().getResource("cardInfo.json")).getFile()
    );
    Gson gson = new Gson();
    JsonReader reader = new JsonReader(new FileReader(file));
    AvailableCards ac = gson.fromJson(reader,AvailableCards.class);

    //SpringApplication.run(Launcher.class, args);
  }
}
