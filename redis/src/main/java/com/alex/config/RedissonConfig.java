package com.alex.config;

import cn.hutool.db.Page;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    public static final int MAX_CONSUMER_THREADS = Runtime.getRuntime().availableProcessors();
    public static final long CONSUMER_POLL_TIME = 300;
    public static final Object DEAD_LETTER_QUEUE = "dead-letter-queue";
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private int database;

    public static final int TIMEOUT = 3000;


    @Bean
    public RedissonClient redissonClient()
    {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password)
                .setDatabase(database)
                .setTimeout(TIMEOUT);
        return Redisson.create(config);
    }


}
