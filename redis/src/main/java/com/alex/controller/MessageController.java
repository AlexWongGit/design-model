package com.alex.controller;

import com.alex.bean.*;
import com.alex.publisher.RedisPublisher;
import com.alex.service.RedisService;
import com.alex.util.RedisStreamQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MessageController {

    @Autowired
    private RedisPublisher redisPublisher;

    @Autowired
    private RedisStreamQueue redisStreamQueue;

    @Autowired
    private RedisService redisService;

    @PostMapping("/publish")
    public String publishMessage(@RequestParam String message, @RequestParam int count) {
        for (int i = 0; i < count; i++) {
            redisPublisher.publish(message);
        }
        return "Message published: " + message;
    }

    @PostMapping("/test")
    public void test() {
        ArrayList<User> users = new ArrayList<>();
        for (int k = 0; k < 10000; k++) {
            User user = new User();
            user.setName("alex");
            user.setAge(18);
            user.setEmail("alex@qq.com");
            user.setBirth(new Date());
            user.setSex("男");
            user.setAddress("中国");
            user.setPhone("123456789");
            user.setPassword("123456");
            Famous famous = new Famous();
            famous.setName("alex");
            famous.setSex("男");
            famous.setBirth(new Date().toString());
            famous.setDeath(new Date().toString());
            user.setFamous(famous);
            Professor professor = new Professor();
            professor.setName("professor");
            professor.setSex("男");
            professor.setBirth(new Date().toString());
            professor.setDeath(new Date().toString());
            user.setProfessor(professor);
            School school = new School();
            school.setName("school");
            school.setAddress("中国");
            user.setSchools(school);
            for (int i = 0; i < 10; i++) {
                Habbit habbit = new Habbit();
                habbit.setName("habbit" + i);
                habbit.setDescription("description" + i);
                habbit.setStatus("status" + i);
                for (int i1 = 0; i1 < 10; i1++) {
                    School school1 = new School();
                    school1.setName("school" + i1);
                    school1.setAddress("中国");
                    habbit.getSchools().add(school1);
                    Famous famous1 = new Famous();
                    famous1.setName("famous" + i1);
                    famous1.setSex("男");
                    famous1.setBirth(new Date().toString());
                    famous1.setDeath(new Date().toString());
                    habbit.getUsers().add(famous1);
                }
                user.getHabbits().add(habbit);
            }

            for (int i = 0; i < 10; i++) {
                Friend friend = new Friend();
                for (int j = 0; j < 10; j++) {
                    Habbit habbit = new Habbit();
                    habbit.setName("habbit" + i);
                    habbit.setDescription("description" + i);
                    habbit.setStatus("status" + i);
                    for (int i1 = 0; i1 < 10; i1++) {
                        School school1 = new School();
                        school1.setName("school" + i1);
                        school1.setAddress("中国");
                        habbit.getSchools().add(school1);
                        Famous famous1 = new Famous();
                        famous1.setName("famous" + i1);
                        famous1.setSex("男");
                        famous1.setBirth(new Date().toString());
                        famous1.setDeath(new Date().toString());
                        habbit.getUsers().add(famous1);
                    }
                    friend.getHabbit().add(habbit);
                }
                friend.setName("friend" + i);
                friend.setAge(18 + i);
                friend.setEmail("friend" + i + "@qq.com");
                friend.setAddress("中国");
                friend.setFamous(famous);
                friend.setProfessor(professor);
                friend.setSchools(school);
                user.getFriends().add(friend);
            }
            users.add(user);
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < users.size(); i++) {
            redisService.saveUser("user:"+i , users.get(i));
            //redisService.saveUserString("user:"+i , users.get(i));
        }
        System.out.println("Time taken: " + (System.currentTimeMillis() - start));

        User retrievedUser = redisService.getUser("user:1");
        //System.out.println("user: " + retrievedUser);
    }

    @PostMapping("/stream")
    public void sendMessage() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("field1", "value1");
        fields.put("field2", "value2");
        RecordId recordId = redisStreamQueue.sendMessage("myStream", fields);
        System.out.println("Message sent with RecordId: " + recordId);
    }

}
