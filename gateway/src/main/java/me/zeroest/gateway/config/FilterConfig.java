package me.zeroest.gateway.config;

import lombok.RequiredArgsConstructor;
import me.zeroest.gateway.filter.CustomFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final CustomFilter customFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/first-service/**")
//                        .filters(f -> f.addRequestHeader("first-request", "first-request-header")
//                                .addResponseHeader("first-response", "first-response-header"))
                        .filters(f -> f.filter(customFilter.apply(new CustomFilter.Config())))
                        .uri("http://localhost:8081"))
                .route(r -> r.path("/second-service/**")
//                        .filters(f -> f.addRequestHeader("second-request", "second-request-header")
//                                .addResponseHeader("second-response", "second-response-header"))
                        .filters(f -> f.filter(customFilter.apply(new CustomFilter.Config())))
                        .uri("http://localhost:8082"))
                .build();
    }

}
