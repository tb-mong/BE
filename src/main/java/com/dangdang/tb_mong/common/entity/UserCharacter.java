package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "User_Character")
public class UserCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isRepresentative;
    private Boolean unlocked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
