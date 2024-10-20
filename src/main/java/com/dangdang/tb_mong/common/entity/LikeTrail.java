package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Like_Trail")
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
}
