package com.dangdang.tb_mong.settings.controller;

import com.dangdang.tb_mong.settings.dto.LocationResponse;
import com.dangdang.tb_mong.settings.service.SettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public LocationResponse setLocation(@RequestParam Long userId,
                                        @RequestParam String locationCode){
        return settingsService.setLocation(userId, locationCode);
    }

    @PostMapping("/set-nickname")
    @Operation(summary = "닉네임 설정")
    public String setNickname(@RequestParam Long userId,
                              @RequestParam String newNickname){
        return settingsService.setNickname(userId, newNickname);
    }
}
