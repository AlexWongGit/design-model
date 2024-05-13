package org.alex.create;

public class MyRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println("创建了异步线程Runnable");
    }
}
