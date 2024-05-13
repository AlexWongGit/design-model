package org.alex.service;

public class Client {
    public static void main(String[] args) {

        Subject subscriptionSubject = new SubscriptionSubject();
        WeiXinUser weiXinUser = new WeiXinUser("猪猪侠");
        WeiXinUser weiXinUser1 = new WeiXinUser("菲菲公主");
        WeiXinUser weiXinUser2 = new WeiXinUser("光头强");
        subscriptionSubject.attach(weiXinUser);
        subscriptionSubject.attach(weiXinUser1);
        subscriptionSubject.attach(weiXinUser2);
        subscriptionSubject.notify("oh GG Bond童话里做英雄！");
    }
}
