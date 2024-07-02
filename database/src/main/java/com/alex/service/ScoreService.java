package com.alex.service;

import com.alex.entity.Score;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ScoreService extends IService<Score> {


    void insertTransaction(Score score);

}
