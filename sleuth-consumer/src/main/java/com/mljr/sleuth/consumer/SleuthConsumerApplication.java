package com.mljr.sleuth.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@SpringBootApplication
@EnableEurekaClient
public class SleuthConsumerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SleuthConsumerApplication.class, args);
	}
}
