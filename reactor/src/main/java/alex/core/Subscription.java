package alex.core;

/**
 * @description 订阅者和发布者之间的关系，需要在当前订阅者的上文中被调用
 * @author wangzf
 * @date 2024/6/18
 */
public interface Subscription {

    /**
     * @description 请求多少数据，最多Long.MAX（2^63-1）
     * @return void1
     * @param var1 请求数据的数量
     */
    void request(long var1);

    /**
     * @description 取消订阅，但调用之后仍有onNext信号发送
     * @return void
     */
    void cancel();
}
