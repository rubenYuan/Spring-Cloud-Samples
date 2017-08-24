package com.mljr.eurekaprovidernode2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class EurekaProviderNode2Application {

	public static void main(String[] args) {
		SpringApplication.run(EurekaProviderNode2Application.class, args);
	}
}
