package com.xiu.consul.consumer;

import com.xiu.consul.consumer.domain.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Mr.xiu
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConsulConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConsulConsumerApplication.class, args);
  }

  @RestController
  static class HelloController {

    @Value("${myName:xiu}")
    private String myName;

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
		private UserConfig userConfig;

    @GetMapping({"/", ""})
    public String hello() {
      return "Hello " + myName + " ,welcome to consul consumer..........";
    }


    /**
     * 获取所有服务
     */
    @GetMapping("/services")
    public Object services() {
      return discoveryClient.getInstances("consul-provider");
    }

    /**
     * 从所有服务中选择一个服务（轮询）
     */
    @GetMapping("/discover")
    public Object discover() {
      return loadBalancer.choose("consul-provider").getUri().toString();
    }

    @GetMapping("/call")
    public String call() {
      ServiceInstance serviceInstance = loadBalancer.choose("consul-provider");
      System.out.println("服务地址：" + serviceInstance.getUri());
      System.out.println("服务名称：" + serviceInstance.getServiceId());
//			System.out.println("配置中心：user"+userConfig.toString());
      String callServiceResult = new RestTemplate()
          .getForObject(serviceInstance.getUri().toString() + "/", String.class);
      System.out.println(callServiceResult);
      return callServiceResult;
    }
  }
}
