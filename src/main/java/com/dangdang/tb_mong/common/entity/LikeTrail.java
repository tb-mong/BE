package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "like_trail")
@Getter
public class LikeTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trail_id")
    private Trail trail;

    public void setUser(User user) {
        this.user = user;
    }

    public void setTrail(Trail trail) {
        this.trail = trail;
    }
}
