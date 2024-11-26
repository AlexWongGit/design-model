package com.alex.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {

    @TableField(value = "id")
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "age")
    private Integer age;

    @TableField(value = "email")
    private String email;

    @TableField(value = "address")
    private String address;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "password")
    private String password;
}
