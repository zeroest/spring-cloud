package me.zeroest.useconfigservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zeroest.useconfigservice.config.ModelSchemas;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfigRefreshListener implements ApplicationListener<EnvironmentChangeEvent> {

    @Value("${test}")
    public String test;

    private final ModelSchemas modelSchemas;

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        log.info("evnet: {}", event);
        Set<String> keys = event.getKeys();
        log.info("keys: {}", keys);

        log.error("schemas: {}", modelSchemas);
        log.error("test: {}", test);

    }
}
