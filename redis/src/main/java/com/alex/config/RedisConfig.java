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
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
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

    public static void main(String[] args) {
        String str1 = "A430FF0034C243EEB06DFF70F0E6F779";
        String str2 = "E21272A33F624569992CF01B087001E9";

        // select * from ma_folder where guid in ('A430FF0034C243EEB06DFF70F0E6F779', 'E21272A33F624569992CF01B087001E9') order by guid desc;
        // 将字符串转换为字节数组
        byte[] bytes1 = str1.getBytes();
        byte[] bytes2 = str2.getBytes();

        // 逐字节比较
        int result = compareByteArrays(bytes1, bytes2);

        if (result > 0) {
            System.out.println(str1 + " > " + str2);
        } else if (result < 0) {
            System.out.println(str1 + " < " + str2);
        } else {
            System.out.println(str1 + " == " + str2);
        }
    }

    // 比较字节数组的方法
    public static int compareByteArrays(byte[] bytes1, byte[] bytes2) {
        int len = Math.min(bytes1.length, bytes2.length);
        for (int i = 0; i < len; i++) {
            if (bytes1[i] != bytes2[i]) {
                return bytes2[i] - bytes1[i]; // 降序排序
            }
        }
        return bytes2.length - bytes1.length; // 如果前len个字节相同，则比较长度
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 设置 Key 的序列化器
        template.setKeySerializer(new StringRedisSerializer());

        // 设置 Value 的序列化器
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //template.setValueSerializer(new JdkSerializationRedisSerializer());
        //template.setValueSerializer(new StringRedisSerializer());

        // 设置 HashKey 的序列化器
        template.setHashKeySerializer(new StringRedisSerializer());

        // 设置 HashValue 的序列化器
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
