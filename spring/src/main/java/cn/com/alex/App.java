package cn.com.alex;

import cn.com.alex.config.AppConfig;
import cn.com.alex.service.OrderService;
import cn.com.alex.service.UserService;
import cn.com.alex.spring.SpringApplicationContext;

public class App {

    public static void main(String[] args) throws Exception {

        //spring容器
        SpringApplicationContext applicationContext = new SpringApplicationContext(AppConfig.class);
        UserService userService = (UserService)applicationContext.getBean("userService");
        OrderService orderService = (OrderService)applicationContext.getBean("orderService");
        userService.test();
        System.out.println(orderService);
    }
}
