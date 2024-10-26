package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "user")
@Getter
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
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repre_character_id")
    private RepreCharacter repreCharacter;

    public void setRepreCharacter(RepreCharacter repreCharacter) {
        this.repreCharacter = repreCharacter;
    }

    public void levelup() {
        this.level += 1;
        this.exp = 0;
    }
}
