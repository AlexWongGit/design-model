package org.alex.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ExecutorConfig {
    @Bean(value = "executor1")
    public Executor executor1() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 一些初始化方法
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean(value = "executor2")
    public Executor executor2() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 一些初始化方法
        taskExecutor.initialize();
        return TtlExecutors.getTtlExecutor(taskExecutor);
    }

    @Bean(value = "executor3")
    public Executor executor3() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 一些初始化方法
        taskExecutor.initialize();
        return TtlExecutors.getTtlExecutor(taskExecutor);
    }
}
