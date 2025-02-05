package com.alex.messaging.publisher;

import com.alex.messaging.error.RedisMessageException;
import com.alex.messaging.model.Message;

import java.util.List;

/**
 * 
 * @author yathiraj
 *
 */
public interface MessagePublisher<T extends Message> {

	void publishMessage(T message);

	void publishMessage(T message, String channelName);

	void publishBatchMessage(List<T> messages, String channelName) throws RedisMessageException;

	void retryPublish(T message, String channelName);

	void retryPublish(List<T> messageList, String channelName);
}
