package com.xiu.config.node1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ConfigNode1Application {
	public static void main(String[] args) {
		SpringApplication.run(ConfigNode1Application.class, args);
	}
}
