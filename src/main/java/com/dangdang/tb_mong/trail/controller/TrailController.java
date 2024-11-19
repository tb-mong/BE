package com.dangdang.tb_mong.trail.controller;

import com.dangdang.tb_mong.common.security.PrincipalDetails;
import com.dangdang.tb_mong.trail.dto.TrailRequest;
import com.dangdang.tb_mong.trail.dto.TrailResponse;
import com.dangdang.tb_mong.trail.service.TrailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trail")
@Tag(name = "산책 기능", description = "산책로를 기록하고 불러오는 API")
public class TrailController {
    private final TrailService trailService;

    @PostMapping("/save")
    @Operation(summary = "산책로 저장")
    public TrailResponse saveTrail(@AuthenticationPrincipal PrincipalDetails userDetails,
                                   @RequestBody TrailRequest trailRequest){
        return trailService.saveTrail(userDetails, trailRequest);
    }

    @GetMapping("/load")
    @Operation(summary = "산책로 불러오기")
    public TrailRequest getTrail(@AuthenticationPrincipal PrincipalDetails userDetails,
                                      @RequestParam Long trailId){
        return trailService.getTrail(userDetails, trailId);
    }

    @GetMapping(value = "/{trailId}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "산책로 이미지 조회하기")
    public Resource getTrailImage(@AuthenticationPrincipal PrincipalDetails userDetails,
                                  @PathVariable Long trailId) {
        return trailService.getTrailImage(userDetails, trailId);
    }

    @PostMapping(value = "/{trailId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "산책로 이미지 저장")
    public ResponseEntity saveTrailImage(@RequestParam MultipartFile file,
                                         @AuthenticationPrincipal PrincipalDetails userDetails,
                                         @PathVariable Long trailId) {
        trailService.saveTrailImage(file, userDetails, trailId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
