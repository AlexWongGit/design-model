package com.alex.rmi.controller.impl;

import com.alex.rmi.controller.PersonController;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientDemo {


    public static void main(String[] args) {

        try {
            //对应写法1
            //客户端去查找指定的服务
/*            PersonController personController = (PersonController) Naming.lookup("rmi://localhost:8888/" + PersonController.class.getName());
            String s = personController.queryName();
            System.out.println("RMI RETURN:"+s);*/


            //对应写法2
            Registry registry = LocateRegistry.getRegistry("localhost", 8888);
            PersonController personController = (PersonController) registry.lookup(PersonController.class.getName());
            System.out.println(personController.queryName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
