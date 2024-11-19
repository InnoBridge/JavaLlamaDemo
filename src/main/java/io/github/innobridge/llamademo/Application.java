package io.github.innobridge.llamademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {
    io.github.innobridge.llamademo.configuration.ApplicationSpecificSpringComponentScanMarker.class,
    io.github.innobridge.llamademo.controller.ApplicationSpecificSpringComponentScanMarker.class
})
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
