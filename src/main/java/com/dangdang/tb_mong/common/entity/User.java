package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long kakaoUuid;
    private String kakaoEmail;
    private String name;
    private String nickname;
    private Integer level;
    private Integer exp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repre_character_id")
    private Character representativeCharacter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
}