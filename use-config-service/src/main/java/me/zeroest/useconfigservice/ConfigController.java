package me.zeroest.useconfigservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ConfigController {

    private final Environment env;

    @GetMapping
    public String getConfig() {
        String token = env.getProperty("token");
        log.error(token);
        return token;
    }

}
