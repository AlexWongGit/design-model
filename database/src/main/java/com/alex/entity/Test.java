package com.alex.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("test")
public class Test {

    @TableField(value = "id")
    private Long id;

    @TableField(value = "name")
    private String name;
}
