package com.alex.messaging.consumer;

import com.alex.messaging.config.RedisConfig;
import com.alex.messaging.error.RedisMessageException;
import com.alex.messaging.listener.RedisMessageListner;
import com.alex.messaging.model.Message;
import com.alex.messaging.publisher.RedisMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 
 * @author yathiraj
 *
 * @param <T>
 */

@Slf4j
public class RedisMessageConsumer<T extends Message> implements MessageConsumer<T> {

	private final ExecutorService executor = new ThreadPoolExecutor(
        Runtime.getRuntime().availableProcessors(),
        Runtime.getRuntime().availableProcessors() + 1,
        0L,
        TimeUnit.MILLISECONDS,
        new ArrayBlockingQueue<>(100),
        new ThreadPoolExecutor.CallerRunsPolicy()
    );

	private static RedisMessagePublisher<Message> deadQueueLetterPublisher;

	private RedissonClient client;

	private Map<String, Set<RedisMessageListner<T>>> listenersMap = new ConcurrentHashMap<>();

	public static <V extends Message> RedisMessageConsumer<V> getNewInstance(Class<V> clazz) {

		if (deadQueueLetterPublisher != null) {
			deadQueueLetterPublisher = RedisMessagePublisher.getNewInstance(Message.class);

		}
		return new RedisMessageConsumer<V>();
	}

	public RedisMessageConsumer() {
		this.client = RedisConfig.getInstance().build();
	}

    @Override
	public T consume(String channelName) throws InterruptedException {
		RBlockingQueue<T> queue = client.getBlockingQueue(channelName);

		T msg = queue.poll(RedisConfig.CONSUMER_POLL_TIME, TimeUnit.SECONDS);
		return msg;
	}

	@Override
	public void consume(String channelName, RedisMessageListner<T> listner) throws RedisMessageException {
		RBlockingQueue<T> queue = client.getBlockingQueue(channelName);
		System.out.println("Consuming messages from " + channelName);
		executor.execute(() -> {
			while (true) {
				T msg = null;
				try {

					msg = queue.poll(RedisConfig.CONSUMER_POLL_TIME, TimeUnit.SECONDS);
					if (msg != null) {
						listner.process(msg);
					}

				} catch (InterruptedException e) {

					if (RedisConfig.enableDeadLetterQueue) {
						RedisMessageConsumer.deadQueueLetterPublisher.publishMessage(msg,
								RedisConfig.DEAD_LETTER_QUEUE);

					}

				}

			}
		});

	}

	@Override
	public void subscribe(String channelName, RedisMessageListner<T> listener) {

		Set<RedisMessageListner<T>> listenerSet = listenersMap.getOrDefault(channelName,
				new HashSet<RedisMessageListner<T>>());
		listenerSet.add(listener);
		executor.execute(() -> {
			try {
				consume(channelName, listener);
			} catch (RedisMessageException e) {
				e.printStackTrace();
			}
		});
		listenersMap.put(channelName, listenerSet);
		log.info("Listener subscribed to " + channelName);
	}

	@Override
	public void subscribe(String channelName, Set<RedisMessageListner<T>> listenrList) {
		listenrList.forEach(item -> subscribe(channelName, item));
	}

}
