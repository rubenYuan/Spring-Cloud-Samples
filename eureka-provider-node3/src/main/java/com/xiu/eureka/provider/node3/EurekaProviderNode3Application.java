package com.xiu.eureka.provider.node3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class EurekaProviderNode3Application {

	public static void main(String[] args) {
		SpringApplication.run(EurekaProviderNode3Application.class, args);
	}
}
