package org.alex.samp;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
/**
 * @description 只用jdk提供的类库，实现并发安全的阻塞队列
 * @author wangzf
 * @date 2024/2/22
 */
public class BlockQueueExample {
    public static void main(String[] args) {
        // 创建一个容量为 10 的阻塞队列
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        // 创建生产者线程
        Thread producerThread = new Thread(() -> {
            try {
                int value = 0;
                while (true) {
                    // 尝试将元素放入队列
                    queue.put(value);
                    System.out.println("Produced: " + value);
                    value++;

                    // 等待一段时间
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 创建消费者线程
        Thread consumerThread = new Thread(() -> {
            try {
                while (true) {
                    // 从队列中取出元素
                    int value = queue.take();
                    System.out.println("Consumed: " + value);

                    // 等待一段时间
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 启动生产者和消费者线程
        producerThread.start();
        consumerThread.start();
    }
}
