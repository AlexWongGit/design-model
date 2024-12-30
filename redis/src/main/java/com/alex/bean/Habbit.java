package com.alex.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Habbit {
    private Long id;
    private String name;
    private String description;
    private String status;

    private List<School> schools = new ArrayList<>();
    private List<Famous> users = new ArrayList<>();
}
