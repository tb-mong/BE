package com.dangdang.tb_mong.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoDto {
    private Long userId;
    private String userEmail;
}
