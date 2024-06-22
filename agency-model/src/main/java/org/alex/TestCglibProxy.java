package org.alex;

import org.alex.consumer.Customer;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class TestCglibProxy {

    /**
     * 基于cglib的动态代理
     * 1.Class type：
     * 指定我们要代理的目标类的字节码对象,也就是指定目标类的类型。
     * 1.callback：
     * 也是一个接口，只是名称定义的作用。只是让别的接口去继承它，提供一个方法它会在合适的时候回来调用它。通常使用其子类
     * 它的子类MethodInterceptor（方法拦截器）接口，其只有一个方法，叫做intercept（）方法
     * intercept（）方法中的参数：
     * 1.proxy:就是代理类对象的一个引用也就是Enharcer.create（）方法的返回值；此引用几乎不会用到。
     * 2.method：对应的就是触发intercept执行的方法的Method对象。假如我们调用了Xxx方法，该方法触发了invoke执行，那么method就是Xxx方法对应的反射对象Method。
     * 3.args：代理对象调用方法时传入的实际参数
     * 4.methodProxy：方法的代理对象，一般不做处理，暂时忽略。
     * @param args
     */
    public static void main(String[] args) {
        // 创建一个目标类对象，也就是顾客对象
        final Customer customer = new Customer();
        // 使用CGLIB创建代理对象
        Customer deliveryClerk = (Customer)Enhancer.create(customer.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                // 判断，如果是order方法，则增强
                if ("order".equals(method.getName())) {
                    Object result = method.invoke(customer, args);
                    System.out.println("已经在送往的路上");
                    return result + "买了一包阳光";
                } else {
                    //使用method反射调用，在原对象中执行方法，并不改变逻辑，也就是说原封不动调用原来的逻辑
                    return method.invoke(customer,args);
                }
            }
        });
        String result = deliveryClerk.order("芝士葡萄奶盖");
        System.out.println(result);
    }
}
