package com.dangdang.tb_mong.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class TrailDto {
    private Long id;
    private String name;
    private String location_name;
    private BigDecimal km;
    private String nickname;
    private int like_count;
    private String image;
}
