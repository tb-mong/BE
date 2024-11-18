package com.dangdang.tb_mong.common.entity;

import com.dangdang.tb_mong.common.enumType.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
    private int total_count;
    private BigDecimal total_km;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    public void levelup() {
        this.level += 1;
        this.exp = 0;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void updateCount() {
        total_count++;
    }

    public void updateKm(BigDecimal km) {
        BigDecimal tmp = km.add(this.total_km);
        this.total_km = tmp;
    }

    public void updateLevel() {
        this.level++;
    }

    public void resetExp() {
        this.exp = 0;
    }

    public void updateExp() {
        this.exp++;
    }
}
