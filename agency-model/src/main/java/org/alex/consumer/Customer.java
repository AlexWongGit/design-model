package org.alex.consumer;

import org.alex.service.OrderInterface;


/**
 * 顾客
 */
public class Customer implements OrderInterface {
    /**
     * 静态代理
     */
    @Override
    public void test1() {

    }

    /**
     * 静态代理
     */
    @Override
    public void test2() {

    }

    @Override
    public String order(String foodName) {
        String action = "王梓峰已经下单点了";
        System.out.println(action + foodName);
        return action + foodName;
    }
}
