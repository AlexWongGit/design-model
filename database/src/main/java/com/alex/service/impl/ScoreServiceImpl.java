package com.alex.service.impl;

import com.alex.entity.Score;
import com.alex.mapper.ScoreMapper;
import com.alex.service.ScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {


    @Override
    public void insertTransaction(Score score) {
        save(score);
        //int i = 1 / 0;
    }
}
