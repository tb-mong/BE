package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "repre_character")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepreCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_character_id", nullable = false)
    private UserCharacter userCharacter;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void setUserCharacter(UserCharacter userCharacter) {
        this.userCharacter = userCharacter;
    }

    public String getImage() {
        return userCharacter.getCharacter().getImage();
    }
}
