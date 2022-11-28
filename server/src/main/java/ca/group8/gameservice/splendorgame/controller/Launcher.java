package ca.group8.gameservice.splendorgame.controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"ca.group8.gameservice.splendorgame"})
public class Launcher extends SpringBootServletInitializer {

  public static void main(String[] args) {

    SpringApplication.run(Launcher.class, args);

  }
}
