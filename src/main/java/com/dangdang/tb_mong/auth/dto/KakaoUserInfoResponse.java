package com.dangdang.tb_mong.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KakaoUserInfoResponse {
    private String uuid;
    private String nickname;
    private String email;
}
