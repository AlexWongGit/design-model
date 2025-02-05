package com.alex.messaging.listener;

@FunctionalInterface
public interface RedisMessageListner<T> {

	public void process(T messageList);
}
