package cn.com.alex.service.impl;

import cn.com.alex.service.OrderService;
import cn.com.alex.service.UserService;
import cn.com.alex.spring.Component;
import cn.com.alex.spring.Scope;
import org.springframework.beans.factory.annotation.Autowired;

@Component("userService")
@Scope("singleton")
public class UserServiceImpl implements UserService {

    @Autowired
    private OrderService orderService;
    @Override
    public void test() {
        System.out.println("hhhh");
    }

    public UserServiceImpl() {

    }

}
