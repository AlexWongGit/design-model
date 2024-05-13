package org.alex;

import org.alex.consumer.Consumer;
import org.alex.consumer.Customer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //1.创建顾客对象
        Customer customer = new Customer();
        //2.创建一级代理对象
        Consumer consumer = new Consumer(customer);
        //2.创建二级代理对象
        DeliveryClerk deliveryClerk = new DeliveryClerk(consumer);
        //3.调用方法
        String result = deliveryClerk.order("鱼香肉丝盖饭");
        System.out.println(result);
    }
}
