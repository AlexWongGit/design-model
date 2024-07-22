package com.alex.rmi.controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PersonController extends Remote {

    String queryName() throws RemoteException;
}
