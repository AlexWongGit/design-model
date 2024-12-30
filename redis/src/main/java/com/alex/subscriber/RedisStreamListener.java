package com.alex.subscriber;

import com.alex.util.RedisStreamQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

@Component
public class RedisStreamListener implements StreamListener<String, ObjectRecord<String, String>> {

    @Autowired
    private RedisStreamQueue redisStreamQueue;

    @Override
    public void onMessage(ObjectRecord<String, String> record) {
        // 处理接收到的消息
        //new RecordId();
        String streamKey = record.getStream();
        String message = record.getValue();
        System.out.println("Received message from stream: " + streamKey + " -> " + message);
        //redisStreamQueue.acknowledgeMessage(streamKey, "group1", record.getId().getValue());
    }
}
