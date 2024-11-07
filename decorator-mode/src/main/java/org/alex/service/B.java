package org.alex.service;

public class B implements  A{


    @Override
    public void a(boolean b) {
        if (b) {
            System.out.println("fsds");
        }
        System.out.println(b);
    }
}
