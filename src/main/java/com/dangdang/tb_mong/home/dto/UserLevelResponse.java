package com.dangdang.tb_mong.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLevelResponse {
    private int level;
    private int exp;
}
