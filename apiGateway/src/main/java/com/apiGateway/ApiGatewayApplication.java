package com.apiGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties
@EnableFeignClients
public class ApiGatewayApplication {

	public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "secrets");
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
