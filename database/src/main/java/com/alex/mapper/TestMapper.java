package com.alex.mapper;

import com.alex.entity.Test;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface TestMapper extends BaseMapper<Test> {
    Test test();
}
