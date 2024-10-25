package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "repre_character")
@Getter
public class RepreCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_character_id", nullable = false)
    private UserCharacter userCharacter;

    public void setUserCharacter(UserCharacter userCharacter) {
        this.userCharacter = userCharacter;
    }

    public String getImage() {
        return userCharacter.getCharacter().getImage();
    }
}
