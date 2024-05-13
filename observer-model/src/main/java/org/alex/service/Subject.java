package org.alex.service;

/**
 * 抽象被观察者
 */

public interface Subject {

    /*
     * 增加订阅者
     * */
    void attach(Observer observer);

    /*
     * 删除订阅者
     * */
    void detach(Observer observer);

    /*
    * 通知订阅者更新消息
    * */
    void notify(String message);
}
