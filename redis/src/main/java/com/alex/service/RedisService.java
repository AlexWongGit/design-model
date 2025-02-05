package com.alex.service;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alex.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveUser(String key, User user) {
        redisTemplate.opsForValue().set(key, user);
    }
    public void saveUserString(String key, User user) {
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(user));
    }

    public void saveUser(String key, String user) {
        redisTemplate.opsForList().leftPush(key, user);
    }
    public void saveUserString(String key, String user) {
        redisTemplate.opsForValue().set(key, user);
    }

    public User getUser(String key) {
        return (User) redisTemplate.opsForValue().get(key);
    }

    public Object getString(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

/*    public String jsonGet(String key, String path) {
        return redisTemplate.execute(connection -> {
            byte[] rawKey = key.getBytes();
            byte[] rawPath = path.getBytes();
            byte[] response = connection.execute("JSON.GET", rawKey, rawPath);
            return response != null ? new String(response) : null;
        });
    }

    public String jsonSet(String key, String path) {
        return redisTemplate.execute(connection -> {
            byte[] rawKey = key.getBytes();
            byte[] rawPath = path.getBytes();
            byte[] response = connection.execute("JSON.SET", rawKey, rawPath);
            return response != null ? new String(response) : null;
        });
    }*/
}
