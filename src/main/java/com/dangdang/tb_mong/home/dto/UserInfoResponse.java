package com.dangdang.tb_mong.home.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    int today_cnt;

    @JsonSerialize(using = ToStringSerializer.class) // 소수점 유지
    double today_km;

    int total_cnt;

    @JsonSerialize(using = ToStringSerializer.class) // 소수점 유지
    double total_km;
}
