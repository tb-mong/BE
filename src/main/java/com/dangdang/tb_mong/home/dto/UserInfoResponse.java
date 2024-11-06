package com.dangdang.tb_mong.home.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private int today_cnt;

    @JsonSerialize(using = ToStringSerializer.class) // 소수점 유지
    private double today_km;

    private int total_cnt;

    @JsonSerialize(using = ToStringSerializer.class) // 소수점 유지
    private double total_km;
}
