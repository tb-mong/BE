package com.dangdang.tb_mong.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLevelResponse {
    int level;
    int exp;
}
