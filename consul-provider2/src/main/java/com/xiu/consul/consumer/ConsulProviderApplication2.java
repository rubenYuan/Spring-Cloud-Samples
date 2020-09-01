package com.xiu.consul.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mr.xiu
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConsulProviderApplication2 {

	public static void main(String[] args) {
		SpringApplication.run(ConsulProviderApplication2.class, args);
	}

	@RestController
	static  class HelloController{

		@GetMapping({"/",""})
		public String hello(){
			return "Hello xiu ,welcome to consul provider node 2..........";
		}
	}
}
