package cn.com.alex.service.impl;

import cn.com.alex.service.UserService;
import cn.com.alex.spring.Component;
import cn.com.alex.spring.Scope;

@Component("userService")
@Scope("singleton")
public class UserServiceImpl implements UserService {

    @Override
    public void test() {
        System.out.println("hhhh");
    }
}
