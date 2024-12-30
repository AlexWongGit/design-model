package com.alex.config;


import com.alex.subscriber.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    public static final int MAX_RETRY = 3;

    @Bean
    MessageListenerAdapter messageListenerAdapterA(RedisSubscriberA subscriber) {
        return new MessageListenerAdapter(subscriber);
    }

    @Bean
    MessageListenerAdapter messageListenerAdapterB(RedisSubscriberB subscriber) {
        return new MessageListenerAdapter(subscriber);
    }

    @Bean
    MessageListenerAdapter messageListenerAdapterC(RedisSubscriberC subscriber) {
        return new MessageListenerAdapter(subscriber);
    }

    @Bean
    MessageListenerAdapter messageListenerAdapterD(RedisSubscriberD subscriber) {
        return new MessageListenerAdapter(subscriber);
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter messageListenerAdapterA,
                                            MessageListenerAdapter messageListenerAdapterB,
                                            MessageListenerAdapter messageListenerAdapterC,
                                            MessageListenerAdapter messageListenerAdapterD) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(messageListenerAdapterA, new ChannelTopic("A"));
        container.addMessageListener(messageListenerAdapterB, new ChannelTopic("B"));
        container.addMessageListener(messageListenerAdapterC, new ChannelTopic("C"));
        container.addMessageListener(messageListenerAdapterD, new ChannelTopic("D"));

        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 设置 Key 的序列化器
        template.setKeySerializer(new StringRedisSerializer());

        // 设置 Value 的序列化器
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //template.setValueSerializer(new StringRedisSerializer());

        // 设置 HashKey 的序列化器
        template.setHashKeySerializer(new StringRedisSerializer());

        // 设置 HashValue 的序列化器
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
