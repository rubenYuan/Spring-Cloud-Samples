package com.xiu.eureka.provider.node1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mr.xiu
 */
@EnableEurekaClient
@SpringBootApplication
public class EurekaProviderNode1Application {

  public static void main(String[] args) {
    SpringApplication.run(EurekaProviderNode1Application.class, args);
  }


  @RestController
  static class IndexController {

    @GetMapping({"", "/"})
    public String index() {
      return "Hi,Mr.xiu! this is  provider-node1 of peer!";
    }
  }
}
