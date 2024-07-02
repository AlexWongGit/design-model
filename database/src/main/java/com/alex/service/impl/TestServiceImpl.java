package com.alex.service.impl;

import com.alex.entity.Score;
import com.alex.entity.Test;
import com.alex.entity.dto.TestDto;
import com.alex.mapper.TestMapper;
import com.alex.service.ScoreService;
import com.alex.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {


    @Resource
    private ScoreService scoreService;

    @Override
    public String test() {
        List<Test> list = this.list();
        return list.get(0).getName();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTransaction(TestDto dto) {
        Test test = new Test();
        BeanUtils.copyProperties(dto,test);
        error(dto);
        save(test);
        int i = 1 / 0;
    }

    private void error(TestDto dto) {
        Score score = new Score();
        score.setScore(dto.getScore());
        scoreService.insertTransaction(score);
    }
}
