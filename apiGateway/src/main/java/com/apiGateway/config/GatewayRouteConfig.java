package com.apiGateway.config;

import org.springframework.cloud.gateway.server.mvc.handler.ProxyExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class GatewayRouteConfig {
    @Bean
    public RouterFunction<ServerResponse> truecallerRoute() {
        return route("truecaller")
                .GET("/truecaller_api/**", http("http://localhost:8083"))
                .POST("/truecaller_api/**", http("http://localhost:8083"))
                .DELETE("/truecaller_api/**", http("http://localhost:8083"))
                .PUT("/truecaller_api/**", http("http://localhost:8083"))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> companyService() {
        return route("company-service")
                .GET("/review_api/**", http("http://localhost:8081"))
                .POST("/review_api/**", http("http://localhost:8081"))
                .DELETE("/review_api/**", http("http://localhost:8081"))
                .PUT("/review_api/**", http("http://localhost:8081"))
                .build();
    }
}
