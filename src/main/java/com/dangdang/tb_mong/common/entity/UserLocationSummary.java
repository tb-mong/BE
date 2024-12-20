package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "user_location_summary")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int count;
    private BigDecimal km;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateCount() {
        this.count++;
    }

    public void updateKm(BigDecimal km) {
        BigDecimal tmp = km.add(this.km);
        this.km = tmp;
    }
}
