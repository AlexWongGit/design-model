package com.alex.controller;

import com.alex.entity.User;
import com.alex.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    @Resource
    private UserService userService;
    @GetMapping("/insert/batch")
    public void batchInsert()
    {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            User user = new User();
            user.setName("alex"+i);
            user.setAge(18);
            user.setEmail("alex"+i+"@qq.com");
            user.setAddress("中国");
            user.setPhone("123456789");
            user.setPassword("123456");
            users.add(user);
        }


        userService.batchInsert(users);
    }


    @GetMapping("/update/batch")
    public void batchUpdate()
    {
        List<User> users = userService.list();

        users.parallelStream().forEach(
                user -> user.setName("alex")
        );


        userService.batchUpdate(users);
    }


    @PostMapping("/upload")
    public String upload(@RequestPart("file") MultipartFile file) {

        if (file.isEmpty()) {
            return "文件为空，上传失败";
        }

        String fileName = file.getOriginalFilename();

        String filePath = "/Users/logs/plm/" + fileName;

        try {
            File dest = new File(filePath);

            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            file.transferTo(dest);

            log.info("文件上传成功，路径: " + filePath);
            return "上传成功";
        } catch (IOException e) {
            e.printStackTrace();
            return "文件上传失败: " + e.getMessage();
        }
    }
}
