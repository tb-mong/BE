package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_character")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
