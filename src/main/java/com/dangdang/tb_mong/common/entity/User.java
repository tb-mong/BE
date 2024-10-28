package com.dangdang.tb_mong.common.entity;

import com.dangdang.tb_mong.common.enumType.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kakaoUuid;
    private String kakaoEmail;
    private String nickname;
    private Integer level;
    private Integer exp;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    public void levelup() {
        this.level += 1;
        this.exp = 0;
    }
}
