package com.alex.subscriber;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriberC implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] bytes) {
        System.out.println("Received message: " + new String(message.getBody()));
    }
}
