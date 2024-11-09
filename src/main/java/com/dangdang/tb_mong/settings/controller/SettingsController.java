package com.dangdang.tb_mong.settings.controller;

import com.dangdang.tb_mong.common.security.PrincipalDetails;
import com.dangdang.tb_mong.settings.dto.LocationResponse;
import com.dangdang.tb_mong.settings.service.SettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
@Tag(name = "설정 기능", description = "동네, 닉네임 설정 API")
@RequiredArgsConstructor
public class SettingsController {
    private final SettingsService settingsService;

    @PostMapping("/set-location")
    @Operation(summary = "동네 설정")
    public LocationResponse setLocation(@AuthenticationPrincipal PrincipalDetails userDetails,
                                        @RequestParam String locationCode){
        return settingsService.setLocation(userDetails, locationCode);
    }

    @PostMapping("/set-nickname")
    @Operation(summary = "닉네임 설정")
    public String setNickname(@AuthenticationPrincipal PrincipalDetails userDetails,
                              @RequestParam String newNickname){
        return settingsService.setNickname(userDetails, newNickname);
    }
}
