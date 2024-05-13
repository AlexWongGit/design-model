package org.alex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class RedissonConfig {

 /*   @Bean
    public Redisson getRedisson() {
        // 1. Create config object
        Config config = new Config();
        config.useClusterServers()
                // use "rediss://" for SSL connection
                .addNodeAddress("redis://127.0.0.1:7181");

// or read config from file
        config = Config.fromYAML(new File("config-file.yaml"));
    }*/
}
