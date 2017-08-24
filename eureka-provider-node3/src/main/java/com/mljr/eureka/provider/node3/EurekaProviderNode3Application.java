package com.mljr.eureka.provider.node3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class EurekaProviderNode3Application {

	public static void main(String[] args) {
		SpringApplication.run(EurekaProviderNode3Application.class, args);
	}
}
