package com.alex.service;

import com.alex.entity.Test;
import com.alex.entity.dto.TestDto;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TestService extends IService<Test> {

    String test();

    void insertTransaction(TestDto dto);
}
