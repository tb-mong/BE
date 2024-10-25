package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "user_character")
@Getter
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

    public void setIsRepresentative(Boolean isRepresentative) {
        this.isRepresentative = isRepresentative;
    }
}
