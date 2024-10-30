package com.dangdang.tb_mong.dongne.controller;

import com.dangdang.tb_mong.common.dto.TrailDto;
import com.dangdang.tb_mong.dongne.dto.LocationNameResponse;
import com.dangdang.tb_mong.dongne.service.DongneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dongne")
@Tag(name = "동네 기능", description = "동네와 관련된 기능을 제공하는 API")
@RequiredArgsConstructor
public class DongneController {
    private final DongneService dongneService;

    @GetMapping("")
    @Operation(summary = "내 동네 조회")
    public LocationNameResponse getLocationName(@RequestParam Long userId){
        return dongneService.getLocationName(userId);
    }

    @GetMapping("/top-user")
    @Operation(summary = "동네별 1위 사용자 조회")
    public String getTopUser(@RequestParam Long userId,
                             @RequestParam Long locationId){
        return dongneService.getTopUser(userId, locationId);
    }

    @GetMapping("/trails")
    @Operation(summary = "동네별 산책로 조회")
    public List<TrailDto> getTrailList(@RequestParam Long userId,
                                       @RequestParam Long locationId){
        return dongneService.getTrailList(userId, locationId);
    }

    @GetMapping("/search")
    @Operation(summary = "동네별 산책로 검색")
    public List<TrailDto> getSearchByLocation(@RequestParam Long userId,
                                              @RequestParam Long locationId,
                                              @RequestParam String trailSortOption,
                                              @RequestParam String keyword){
        return dongneService.getSearchByLocation(userId, locationId, trailSortOption, keyword);
    }

    @PostMapping("/like")
    @Operation(summary = "산책로 좋아요")
    public TrailDto likeTrail(@RequestParam Long userId,
                              @RequestParam Long trailId){
        return dongneService.likeTrail(userId, trailId);
    }
}
