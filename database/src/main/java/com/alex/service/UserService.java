package com.alex.service;

import com.alex.entity.Test;
import com.alex.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {


    void batchInsert(List<User> users);

    void batchUpdate(List<User> users);
}
