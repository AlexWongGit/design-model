package org.alex;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.WeakHashMap;

public class ThreadLocalTest {

    /**
     * ThreadLocal
     *
     * 场景：它的应用就是比如当前用户特有的一些属性，不能跟其他线程，用户共用。
     */
    private static final ThreadLocal<Integer> oldThreadLocal = new ThreadLocal<>();

    private static final InheritableThreadLocal<Integer> tlTheadLocal = new InheritableThreadLocal<>();

    /**
     * TransmittableThreadLocal
     *
     * 场景：就是父子线程或者不同线程需要共用一些变量。
     * 举个例子：之前在肥朝哥群里，肥大一直叼胃口，因为全链路日志是自研的嘛，
     * 没有sleuth等等框架的支持，所以像MDC这些全局变量，在多线程里头就失效了。
     */
    private static final TransmittableThreadLocal<Integer> ttlThreadLocal = new TransmittableThreadLocal<>();

    public static void main(String[] args) {

    }
}
