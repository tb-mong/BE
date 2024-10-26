package com.dangdang.tb_mong.record.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class trailDto {
    Long id;
    String name;
    String location_name;
    BigDecimal km;
    int like_count;
    String image;
}
