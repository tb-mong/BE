package com.dangdang.tb_mong.auth.controller;

import com.dangdang.tb_mong.auth.service.KakaoAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "로그인 및 회원가입", description = "카카오 로그인 및 회원가입 API")
@RequiredArgsConstructor
public class AuthController {
    private final KakaoAuthService kakaoAuthService;

    // 카카오 로그인 처리 후 JWT 토큰 발급
    @GetMapping("/kakao")
    @Operation(summary = "")
    public String handleKakaoLogin(@RequestParam String kakaoAccessToken) {
        String result = kakaoAuthService.handleKakaoAuth(kakaoAccessToken);

        return result;
    }

    // 회원가입 처리 후 JWT 토큰 발급
    @PostMapping("/signup")
    @Operation(summary = "")
    public String signUpUser(@RequestParam String kakaoAccessToken,
                             @RequestParam String locationCode) {
        String result = kakaoAuthService.signUpUser(kakaoAccessToken, locationCode);
        return result;
    }
}
