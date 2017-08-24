package com.mljr.sleuth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin.server.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer //收集spans,通过zipkin-UI查询
@EnableEurekaClient
public class SleuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SleuthServerApplication.class, args);
	}
}
