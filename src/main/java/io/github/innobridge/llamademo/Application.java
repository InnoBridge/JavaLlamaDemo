package io.github.innobridge.llamademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {
    io.github.innobridge.llamademo.configuration.ApplicationSpecificSpringComponentScanMarker.class,
    io.github.innobridge.llamademo.controller.ApplicationSpecificSpringComponentScanMarker.class
})
public class Application {

  public static void main(String[] args) {
    // Set the native library path for CUDA support
    System.setProperty("io.github.innobridge.llama.lib.path", 
        "/home/yi/Documents/JavaLlama/JavaLlama/src/main/resources_linux_cuda/io/github/innobridge/llama/Linux/x86_64");
    
    SpringApplication.run(Application.class, args);
  }

}
