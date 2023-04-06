package ca.group8.gameservice.splendorgame.controller;

import java.io.FileNotFoundException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Launches everything.
 */
@SpringBootApplication(scanBasePackages = {"ca.group8.gameservice.splendorgame"})
public class Launcher extends SpringBootServletInitializer {

  /**
   * Constructor of spring boot application launcher.
   */
  public Launcher() {}

  /**
   * start point of spring boot application.
   *
   * @param args arguments
   * @throws FileNotFoundException file not found exception.
   */
  public static void main(String[] args) throws FileNotFoundException {

    SpringApplication.run(Launcher.class, args);

  }
}
