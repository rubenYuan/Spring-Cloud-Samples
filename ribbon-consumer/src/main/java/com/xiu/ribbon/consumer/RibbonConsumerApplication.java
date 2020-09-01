package com.xiu.ribbon.consumer;

import javax.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Mr.xiu
 */
@SpringBootApplication
@EnableEurekaClient
public class RibbonConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(RibbonConsumerApplication.class, args);
  }

  @RestController
  static class RibbonConsumerController {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
      return new RestTemplate();
    }

    @Resource
    private RestTemplate restTemplate;

    /**
     * info
     */
    @GetMapping({"/", ""})
    public String info() {
      return "Hi,Mr.xiu,this is ribbon-consumer";
    }

    /**
     * 远程服务ribbon rest
     */
    @GetMapping("/index")
    public Object ribbonIndex() {
      String str = restTemplate.getForEntity("http://eureka-provider/", String.class).getBody();
      System.out.println("<<<<<<<<<<<<<ribbon返回值:" + str + ">>>>>>>>>>>>>>>>>");
      return str;
    }
  }
}