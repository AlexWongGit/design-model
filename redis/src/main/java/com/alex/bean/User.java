package com.alex.bean;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Integer age;

    private String email;

    private String address;

    private String phone;

    private String password;

    private Date birth;

    private String sex;

    private List<Friend> friends = new ArrayList<>();

    private Famous famous;

    private List<Habbit> habbits = new ArrayList<>();

    private Professor professor;

    private School schools;
}
