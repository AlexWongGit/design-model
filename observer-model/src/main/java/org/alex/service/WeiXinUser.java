package org.alex.service;


/**
 * 具体观察者
 */
public class WeiXinUser implements Observer {

    //用户名
    private String userName;

    public WeiXinUser(String userName) {
        this.userName = userName;
    }

    @Override
    public void update(String message) {
        System.out.println(userName+"收到了"+message);
    }
}
