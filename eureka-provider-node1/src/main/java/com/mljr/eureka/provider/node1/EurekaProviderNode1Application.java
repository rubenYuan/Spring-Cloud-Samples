package com.mljr.eureka.provider.node1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableDiscoveryClient //该注解能实现服务发现,Spring Cloud的服务发现包含了Eureka，Consul，Zookeeper
//@EnableEurekaClient //该注解只能实现Eureka服务的发现
@SpringBootApplication
public class EurekaProviderNode1Application {

	public static void main(String[] args) {
		SpringApplication.run(EurekaProviderNode1Application.class, args);
	}
}
