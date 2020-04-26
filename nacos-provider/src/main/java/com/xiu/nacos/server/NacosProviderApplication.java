package com.xiu.nacos.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mr.xiu
 */
@EnableDiscoveryClient
@SpringBootApplication
public class NacosProviderApplication {

  public static void main(String[] args) {
    SpringApplication.run(NacosProviderApplication.class, args);
  }

  @RestController
  static class TestController {

    @GetMapping(value = {"","/"})
    public String index() {
      return "hello nacos provider";
    }
  }
}

