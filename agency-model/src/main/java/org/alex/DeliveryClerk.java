package org.alex;


import org.alex.service.OrderInterface;

/**
 * 外卖员（静态代理）
 */
public class DeliveryClerk implements OrderInterface{

    //把原来的对象传入并保存到成员位置。也就是目标类对象
    //把下单用户信息保存
    private OrderInterface source;

    public DeliveryClerk(OrderInterface source) {
        this.source =source;
    }

    /**
     * 加强，多一杯奶茶
     * @param foodName
     * @return
     */
    @Override
    public String order(String foodName) {
        String result = source.order(foodName);
        System.out.println("外卖员已经接"+foodName+"订单在送去的路上");
        return result+"又买了一杯奶茶";
    }

    @Override
    public void test1() {

    }

    @Override
    public void test2() {

    }
}
