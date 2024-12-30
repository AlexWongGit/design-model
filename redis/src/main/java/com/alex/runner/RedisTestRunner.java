//package com.alex.runner;
//
//import com.alex.bean.*;
//import com.alex.service.RedisService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//public class RedisTestRunner implements CommandLineRunner {
//
//    @Autowired
//    private RedisService redisService;
//    @Override
//    public void run(String... args) throws Exception {
//        long start = System.currentTimeMillis();
//        for (int k = 0; k < 100; k++) {
//            User user = new User();
//            user.setName("alex");
//            user.setAge(18);
//            user.setEmail("alex@qq.com");
//            user.setBirth(new Date());
//            user.setSex("男");
//            user.setAddress("中国");
//            user.setPhone("123456789");
//            user.setPassword("123456");
//            Famous famous = new Famous();
//            famous.setName("alex");
//            famous.setSex("男");
//            famous.setBirth(new Date().toString());
//            famous.setDeath(new Date().toString());
//            user.setFamous(famous);
//            Professor professor = new Professor();
//            professor.setName("professor");
//            professor.setSex("男");
//            professor.setBirth(new Date().toString());
//            professor.setDeath(new Date().toString());
//            user.setProfessor(professor);
//            School school = new School();
//            school.setName("school");
//            school.setAddress("中国");
//            user.setSchools(school);
//            for (int i = 0; i < 10; i++) {
//                Habbit habbit = new Habbit();
//                habbit.setName("habbit" + i);
//                habbit.setDescription("description" + i);
//                habbit.setStatus("status" + i);
//                for (int i1 = 0; i1 < 10; i1++) {
//                    School school1 = new School();
//                    school1.setName("school" + i1);
//                    school1.setAddress("中国");
//                    habbit.getSchools().add(school1);
//                    Famous famous1 = new Famous();
//                    famous1.setName("famous" + i1);
//                    famous1.setSex("男");
//                    famous1.setBirth(new Date().toString());
//                    famous1.setDeath(new Date().toString());
//                    habbit.getUsers().add(famous1);
//                }
//                user.getHabbits().add(habbit);
//            }
//
//            for (int i = 0; i < 10; i++) {
//                Friend friend = new Friend();
//                for (int j = 0; j < 10; j++) {
//                    Habbit habbit = new Habbit();
//                    habbit.setName("habbit" + i);
//                    habbit.setDescription("description" + i);
//                    habbit.setStatus("status" + i);
//                    for (int i1 = 0; i1 < 10; i1++) {
//                        School school1 = new School();
//                        school1.setName("school" + i1);
//                        school1.setAddress("中国");
//                        habbit.getSchools().add(school1);
//                        Famous famous1 = new Famous();
//                        famous1.setName("famous" + i1);
//                        famous1.setSex("男");
//                        famous1.setBirth(new Date().toString());
//                        famous1.setDeath(new Date().toString());
//                        habbit.getUsers().add(famous1);
//                    }
//                    friend.getHabbit().add(habbit);
//                }
//                friend.setName("friend" + i);
//                friend.setAge(18 + i);
//                friend.setEmail("friend" + i + "@qq.com");
//                friend.setAddress("中国");
//                friend.setFamous(famous);
//                friend.setProfessor(professor);
//                friend.setSchools(school);
//                user.getFriends().add(friend);
//            }
//            redisService.saveUser("user:"+k , user);
//        }
//        System.out.println("Time taken: " + (System.currentTimeMillis() - start));
//
//        User retrievedUser = redisService.getUser("user:1");
//        System.out.println("user: " + retrievedUser);
//    }
//}
