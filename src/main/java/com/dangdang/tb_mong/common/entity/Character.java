package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "`character`")
@Getter
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer levelRequired;
    private String image;
    private String name;
}

