package me.zeroest.gateway.config;

/*
* https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#httptrace-endpoint-renamed-to-httpexchanges
* Related infrastructure classes have also been renamed. For example, HttpTraceRepository is now named HttpExchangeRepository and can be found in the org.springframework.boot.actuate.web.exchanges package.
* */

//import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
//import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpTraceConfig {

    @Bean
    public HttpExchangeRepository httpTraceRepository() {
        return new InMemoryHttpExchangeRepository();
    }

}
