package ca.group8.gameservice.splendorgame.controller;
import ca.group8.gameservice.splendorgame.model.splendormodel.DevelopmentCard;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.util.ResourceUtils;

@SpringBootApplication(scanBasePackages = {"ca.group8.gameservice.splendorgame"})
public class Launcher extends SpringBootServletInitializer {

  public static void main(String[] args) {

    SpringApplication.run(Launcher.class, args);

  }
}
