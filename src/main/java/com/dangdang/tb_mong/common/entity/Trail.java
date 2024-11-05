package com.dangdang.tb_mong.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.sql.Date;

@Entity
@Table(name = "trail")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Trail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String image;
    private BigDecimal km;
    private LocalTime pace;
    private LocalTime time;
    private BigDecimal perHour;
    private Integer likeCnt;
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    public void setLikeCnt(int i) {
        this.likeCnt = i;
    }

    public void imageUpdate(String newImageName) {
        this.image = newImageName;
    }
}

