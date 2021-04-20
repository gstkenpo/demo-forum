package com.forum.gateway.config;

import com.forum.gateway.filter.AdminFilter;
import com.forum.gateway.filter.JWTAuthorizationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {
    @Autowired JWTAuthorizationFilter jwtAuthorizationFilter;
    @Autowired AdminFilter adminFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                    .route("member-service", r -> r.path("/rest/member").or()
                                                    .path("/rest/login").filters(f -> f.filter(jwtAuthorizationFilter)).uri("lb://member-service"))
                    .route("admin-service", r -> r.path("/rest/admin").filters(f -> f.filter(jwtAuthorizationFilter).filter(adminFilter)).uri("lb://member-service"))
                    .build();
    }
}
