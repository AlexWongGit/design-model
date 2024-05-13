package org.alex.genericity;

import java.util.ArrayList;
import java.util.List;

public class Test {

    // 这是一个简单的泛型方法
    public static <T> T add(T x, T y) {
        return y;
    }

    public static void main(String[] args) {
        // 一、不显式地指定类型参数
        //（1）传入的两个实参都是 Integer，所以泛型方法中的<T> == <Integer>
        int i = Test.add(1, 2);

        //（2）传入的两个实参一个是 Integer，另一个是 Float，
        // 所以<T>取共同父类的最小级，<T> == <Number>
        Number f = Test.add(1, 1.2);

        // 传入的两个实参一个是 Integer，另一个是 String，
        // 所以<T>取共同父类的最小级，<T> == <Object>
        Object o = Test.add(1, "asd");

        // 二、显式地指定类型参数
        //（1）指定了<T> = <Integer>，所以传入的实参只能为 Integer 对象
        int a = Test.<Integer>add(1, 2);

        //（2）指定了<T> = <Integer>，所以不能传入 Float 对象
        //int b = Test.<Integer>add(1, 2.2);// 编译错误

        //（3）指定<T> = <Number>，所以可以传入 Number 对象
        // Integer 和 Float 都是 Number 的子类，因此可以传入两者的对象
        Number c = Test.<Number>add(1, 2.2);

        //类型擦除
        System.out.println(typeErasure());


        ArrayList<? super Number> listSuper = new ArrayList<>();
        ArrayList<? extends Number> list = new ArrayList<>();
        ArrayList<Object> listObj = new ArrayList<>();
        ArrayList<Integer> listInt = new ArrayList<>();

        listSuper.add(new Integer(1));// 编译正确
        listSuper.add(new Float(1.0));// 编译正确

        //listObj = listSuper;

        //listInt = listSuper;
        // Object 是 Number 的父类
        //listSuper.add(new Object());// 编译错误
        //fillNumList(listInt);// 编译错误

        System.out.println(Integer.toHexString(0x0201));

    }

    public static boolean typeErasure() {
        ArrayList<String> arrayString = new ArrayList<String>();
        ArrayList<Integer> arrayInteger = new ArrayList<Integer>();
        return arrayString.getClass() == arrayInteger.getClass();// true
    }


    public static void fillNumList(ArrayList<? super Number> list) {
        list.add(new Integer(0));// 编译正确
        list.add(new Float(1.0));// 编译正确

        // 遍历传入集合中的元素，并赋值给 Number 对象；会编译错误
        /*for (Number number : list) {
            System.out.print(number.intValue() + " ");
            System.out.println();
        }*/
        // 遍历传入集合中的元素，并赋值给 Object 对象；可以正确编译
        // 但只能调用 Object 类的方法，不建议这样使用
        for (Object obj : list) {
            System.out.println(obj);
        }
    }
}