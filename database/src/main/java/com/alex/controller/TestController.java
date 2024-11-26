package com.alex.controller;


import com.alex.entity.User;
import com.alex.entity.dto.TestDto;
import com.alex.service.TestService;
import com.alex.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @Resource
    private TestService testService;


    @GetMapping("/test")
    public String test()
    {
        return testService.test();
    }


    @PostMapping("/insert/transaction")
    public void transaction(@RequestBody TestDto dto)
    {
        testService.insertTransaction(dto);
    }


}
