package com.alex.mapper;

import com.alex.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UserMapper extends BaseMapper<User> {

    void save(User user);

    void update(User user);
}
