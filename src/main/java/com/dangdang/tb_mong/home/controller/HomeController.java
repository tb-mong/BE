package com.dangdang.tb_mong.home.controller;

import com.dangdang.tb_mong.common.Service.ImageService;
import com.dangdang.tb_mong.common.security.PrincipalDetails;
import com.dangdang.tb_mong.home.dto.UserInfoResponse;
import com.dangdang.tb_mong.home.dto.UserLevelResponse;
import com.dangdang.tb_mong.home.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
@Tag(name = "홈 관련 기능", description = "사용자 정보, 레벨 및 대표 캐릭터 이미지를 처리하는 API")
public class HomeController {
    private final ImageService imageService;
    private final HomeService homeService;

    @GetMapping(value = "/repre-character", produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "대표 캐릭터 조회")
    public Resource getImage(@AuthenticationPrincipal PrincipalDetails userDetails) {
        return imageService.getImageByUserId(userDetails);
    }

    @GetMapping("/level")
    @Operation(summary = "레벨, 경험치 조회")
    public UserLevelResponse getLevel(@AuthenticationPrincipal PrincipalDetails userDetails) {
        return homeService.getLevel(userDetails);
    }

    @PostMapping("/levelup")
    @Operation(summary = "레벨업 이벤트")
    public UserLevelResponse levelupEvent(@AuthenticationPrincipal PrincipalDetails userDetails) {
        return homeService.levelupEvent(userDetails);
    }

    @GetMapping("/info")
    @Operation(summary = "사용자 산책 정보")
    public UserInfoResponse getUserInfo(@AuthenticationPrincipal PrincipalDetails userDetails){
        return homeService.getUserInfo(userDetails);
    }
}
