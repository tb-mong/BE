package com.dangdang.tb_mong.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    int today_cnt;
    double today_km;
    int total_cnt;
}
