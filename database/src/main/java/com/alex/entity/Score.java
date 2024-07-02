package com.alex.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("score")
public class Score {

    private Long id;

    private Long score;
}
