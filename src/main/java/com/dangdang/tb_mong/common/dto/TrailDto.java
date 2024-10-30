package com.dangdang.tb_mong.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class TrailDto {
    Long id;
    String name;
    String location_name;
    BigDecimal km;
    String nickname;
    int like_count;
    String image;
}
