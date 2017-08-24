package com.mljr.config.node1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient //开启服务注册
@SpringBootApplication
public class ConfigNode1Application {

	public static void main(String[] args) {
		SpringApplication.run(ConfigNode1Application.class, args);
	}
}
