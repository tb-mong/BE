package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;
}

