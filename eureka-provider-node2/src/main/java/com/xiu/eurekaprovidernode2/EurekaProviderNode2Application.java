package com.xiu.eurekaprovidernode2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class EurekaProviderNode2Application {

	public static void main(String[] args) {
		SpringApplication.run(EurekaProviderNode2Application.class, args);
	}
}
