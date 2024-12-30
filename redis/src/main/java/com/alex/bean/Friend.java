package com.alex.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Friend {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private String address;

    private Famous famous;
    private Professor professor;


    private List<Habbit> habbit = new ArrayList<>();

    private School schools;

}
