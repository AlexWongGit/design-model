package org.alex;

import org.alex.consumer.Customer;
import org.alex.service.OrderInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestJDKProxy {


    /**
     *
     * 基于JDK的动态代理
     * 1.ClassLoader loader：
     *
     * 固定写法，指定目标类对象的类的加载器即可，用于加载目标类及其接口的字节码文件，通常使用目标类的字节码文件调用getClassLoader（)方法可得到。
     *
     * 2.Class<?>[] interfaces:
     *
     * 固定写法，指定目标类的所以接口的字节码对象的数组，通常使用目标类的字节码文件调用getInterfaces（）方法可得到。
     *
     * 3.InvocationHander h：
     *
     * 这个参数是一个接口，主要关注它里面的一个方法，它会在代理类调用方法时执行，也就是说，在代理类对象中调用的任何方法都会执行invoke（）方法。所以在此方法中进行代码的扩展。
     *
     * invoke()方法中参数的含义：
     *
     * 1.proxy:就是代理类对象的一个引用也就是Proxy.newProxyInstance（）方法的返回值；此引用几乎不会用到。
     * 2.method：对应的就是触发invoke执行的方法的Method对象。假如我们调用了Xxx方法，该方法触发了invoke执行，那么method就是Xxx方法对应的反射对象Method。
     * 3.args：代理对象调用方法时传入的实际参数
     * @param args
     */
    public static void main(String[] args) {
        final Customer customer = new Customer();
        OrderInterface deliveryClerk = (OrderInterface)Proxy.newProxyInstance(customer.getClass().getClassLoader(), customer.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("order".equals(method.getName())) {
                    Object result = method.invoke(customer, args);
                    System.out.println("骑手已经接到订单，在送达的路上。");
                    return result + "买了一杯茶百道";
                } else {
                    return method.invoke(customer, args);
                }
            }
        });
        String result = deliveryClerk.order("海盐芝士蛋糕");
        System.out.println(result);
    }
}
