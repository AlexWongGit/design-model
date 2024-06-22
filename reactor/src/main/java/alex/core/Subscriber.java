package alex.core;


/**
 * @description 只能存在一个活跃订阅
 * @author wangzf
 * @date 2024/6/18
 */
public interface Subscriber<T> {

    void onSubscribe(Subscription var1);

    void onNext(T var1);

    void onError(Throwable var1);

    void onComplete();
}
