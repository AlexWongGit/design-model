package com.alex.service.impl;

import com.alex.entity.User;
import com.alex.mapper.UserMapper;
import com.alex.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public void batchInsert(List<User> users) {
        log.info("开始批量插入");
        users.parallelStream().forEach(user -> this.baseMapper.save(user));
        log.info("批量插入成功");
    }

    @Override
    public void batchUpdate(List<User> users) {
        log.info("开始批量更新");
        users.parallelStream().forEach(user -> this.baseMapper.update(user));
        log.info("批量更新成功");
    }
}
