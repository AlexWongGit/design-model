package com.alex.messaging.consumer;

import com.alex.messaging.error.RedisMessageException;
import com.alex.messaging.listener.RedisMessageListner;
import com.alex.messaging.model.Message;
import reactor.util.annotation.NonNull;

import java.util.Set;

/**
 * 
 * @author yathiraj
 *
 */
public interface MessageConsumer<T extends Message> {

	T consume(@NonNull String channelName) throws InterruptedException;

	void consume(@NonNull String channelName, RedisMessageListner<T> listner) throws RedisMessageException;

	void subscribe(@NonNull String channelName, RedisMessageListner<T> listener);

	void subscribe(@NonNull String channelName, Set<RedisMessageListner<T>> listenrList);

}
