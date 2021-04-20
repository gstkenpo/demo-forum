package com.forum.gateway.config;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Component
public class RouterValidator {
    @Data
    @AllArgsConstructor
    class EndPoint {
        String url;
        HttpMethod method;
    }
    public final List<EndPoint> openApiEndpoints = List.of(
        new EndPoint("/rest/member", HttpMethod.POST),
        new EndPoint("/rest/login", HttpMethod.POST)
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(endPoint -> request.getURI().getPath().contains(endPoint.getUrl())
                    && request.getMethod().equals(endPoint.getMethod()));
}
