package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "spot")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 위도
    private double la;
    // 경도
    private double lo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trail_id")
    private Trail trail;
}
