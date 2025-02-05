package com.alex.messaging.publisher;

import com.alex.messaging.config.RedisConfig;
import com.alex.messaging.error.RedisMessageException;
import com.alex.messaging.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;

import java.util.List;
import java.util.UUID;

/**
 * 
 * @author yathiraj
 *
 */
@Slf4j
public class RedisMessagePublisher<T extends Message> implements MessagePublisher<T> {

	private RedissonClient client;

	public static <V extends Message> RedisMessagePublisher<V> getNewInstance(Class<V> clazz) {
		return new RedisMessagePublisher<V>();
	}

	public RedisMessagePublisher() {
		client = RedisConfig.getInstance().build();

	}

	@Override
	public void publishMessage(T message) {
		publishMessage(message, message.getChannelName());
	}

	@Override
	public void publishMessage(T message, String channelName) {

		if (!message.isEmpty()) {
			RQueue<Message> queue = client.getQueue(channelName);
            message.setId(generateId());
			RFuture<Boolean> resFuture = queue.addAsync(message);
			resFuture.onComplete((res, e) -> {
				log.info("Message has been successfully published Message Id : " + message.getId());
				if (!res) {
					if (e != null) {
						e.printStackTrace();
					}
					retryPublish(message, channelName);
				} else {
                    log.info("Successfully pushed to ");
				}
			});

		} else {
			log.warn("Trying to pubish message with empty body");
		}

	}

	@Override
	public void publishBatchMessage(List<T> messages, String channelName) throws RedisMessageException {

		if (messages.isEmpty()) {
			log.warn("Empty messages are not allowed");
			throw new RedisMessageException("Emtoy message list");
		}

		RBatch batch = client.createBatch();
		RQueueAsync<Message> queue = batch.getQueue(channelName);
		queue.addAllAsync(messages);
		RFuture<BatchResult<?>> futureList = batch.executeAsync();

		futureList.onComplete((res, e) -> {

			System.out.println("Message has been successfully published ");

		});

	}

	@Override
	public void retryPublish(T message, String channelName) {
		int i = 0;
		while (i < RedisConfig.MAX_RETRY) {
			System.out.println("Retrying publish of " + message.getId());
			i++;
			RQueue<Message> queue = client.getQueue(channelName);
			message.setId(generateId());
			boolean res = queue.add(message);
			if (res) {
				return;
			}
		}

	}

	private String generateId() {
		return UUID.randomUUID().toString();
	}

	@Override
	public void retryPublish(List<T> messageList, String channelName) {
		// TODO Auto-generated method stub

	}

}
