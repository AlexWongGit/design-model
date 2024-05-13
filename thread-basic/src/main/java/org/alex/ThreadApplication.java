package org.alex;

import org.alex.create.MyRunnable;

public class ThreadApplication {

    public static void main(String[] args) {
        Thread thread = new Thread(new MyRunnable());
        thread.start();
        System.out.println("主线程");
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("异步线程Runnable");
            }
        });
        thread1.start();
        Thread threadLambdaRunnable = new Thread(() -> {
            System.out.println("ThreadLambdaRunnable");
        });
        threadLambdaRunnable.start();
    }
}
