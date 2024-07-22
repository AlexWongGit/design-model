package com.alex.rmi.controller.impl;

import com.alex.rmi.controller.PersonController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class PersonControllerImpl extends UnicastRemoteObject implements PersonController {


    protected PersonControllerImpl() throws RemoteException {
    }

    @Override
    public String queryName() throws RemoteException {
        System.out.println("Receive request");
        return "My name's CJ";
    }

    public static void main(String[] args) {
        try {

            //写法1
            //创建服务端
            PersonController personController = new PersonControllerImpl();
            //注册到8888端口，也注册可以注册到别的机器上。
            LocateRegistry.createRegistry(8888);
            //绑定服务端到指定的地址，这里的localhost对应的上一步注册端口号的机器
            java.rmi.Naming.rebind("rmi://localhost:8888/" + PersonController.class.getName(), personController);
            System.out.println("Ready...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
