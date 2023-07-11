package me.zeroest.useconfigservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RefreshScope
@Data
@Component
@ConfigurationProperties(prefix = "model")
public class ModelSchemas {

    private List<Schema> schemas = new ArrayList<>();

    @Data
    public static class Schema {
        private String name;
        private String path;
    }
}

/*
model:
  schemas:
    - name: exmi-xgboost
      path: xgboost.bin
    - name: exmi-lightgbm
      path: lightgbm.bin
*/