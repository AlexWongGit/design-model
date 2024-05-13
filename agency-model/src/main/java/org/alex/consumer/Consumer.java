package org.alex.consumer;

import org.alex.service.OrderInterface;

public class Consumer implements OrderInterface {

    private OrderInterface orderInterface;

    public Consumer(OrderInterface orderInterface) {
        this.orderInterface = orderInterface;
    }

    @Override
    public void test1() {

    }

    @Override
    public void test2() {

    }

    @Override
    public String order(String foodName) {
        String result = orderInterface.order(foodName);
        return result + foodName;
    }
}
