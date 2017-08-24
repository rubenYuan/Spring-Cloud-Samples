package com.mljr.config.node2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ConfigNode2Application {

	public static void main(String[] args) {
		SpringApplication.run(ConfigNode2Application.class, args);
	}
}
