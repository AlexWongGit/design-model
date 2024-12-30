package com.alex.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RedisStreamQueue {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 发送消息到指定 Stream
     *
     * @param streamKey Stream 的 Key
     * @param message   消息内容
     * @return 消息的 RecordId
     */
    public RecordId sendMessage(String streamKey, Object message) {
        ObjectRecord<String, Object> record = ObjectRecord.create(streamKey, message);
        return redisTemplate.opsForStream().add(record);
    }

    /**
     * 发送消息到指定 Stream，并指定消息字段
     *
     * @param streamKey Stream 的 Key
     * @param fields    消息字段
     * @return 消息的 RecordId
     */
    public RecordId sendMessage(String streamKey, Map<String, Object> fields) {
        MapRecord<String, String, Object> record = MapRecord.create(streamKey, fields);
        return redisTemplate.opsForStream().add(record);
    }

    /**
     * 从指定 Stream 读取消息
     *
     * @param streamKey Stream 的 Key
     * @param consumerGroup 消费者组
     * @param consumerName  消费者名称
     * @param count         读取的消息数量
     * @return 读取到的消息列表
     */
    public List<MapRecord<String, Object, Object>> readMessages(String streamKey, String consumerGroup, String consumerName, int count) {
        StreamOffset<String> offset = StreamOffset.create(streamKey, ReadOffset.lastConsumed());
        return redisTemplate.opsForStream().read(Consumer.from(consumerGroup, consumerName), StreamReadOptions.empty().count(count), offset);
    }

    /**
     * 创建消费者组
     *
     * @param streamKey Stream 的 Key
     * @param groupName 消费者组名称
     */
    public void createConsumerGroup(String streamKey, String groupName) {
        redisTemplate.opsForStream().createGroup(streamKey, groupName);
    }

    /**
     * 确认消息已处理
     *
     * @param streamKey Stream 的 Key
     * @param groupName 消费者组名称
     * @param recordId  消息的 RecordId
     */
    public void acknowledgeMessage(String streamKey, String groupName, String recordId) {
        redisTemplate.opsForStream().acknowledge(streamKey, groupName, recordId);
    }
}
