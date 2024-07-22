package com.alex.rmi.controller.impl;

import com.alex.rmi.controller.PersonController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class PersonControllerImpl2  extends UnicastRemoteObject implements PersonController {

    protected PersonControllerImpl2() throws RemoteException {
    }

    @Override
    public String queryName() throws RemoteException {
        System.out.println("Receive request");
        return "My name's RMI";
    }


    public static void main(String[] args) {
        try {
            //写法2
            //创建服务端
            PersonController personController = new PersonControllerImpl2();
            //注册到8888端口，也注册可以注册到别的机器上。
            Registry registry = LocateRegistry.createRegistry(8888);
            registry.rebind(PersonController.class.getName(), personController);
            System.out.println("Ready...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
