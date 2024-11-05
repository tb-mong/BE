package com.dangdang.tb_mong.trail.controller;

import com.dangdang.tb_mong.trail.dto.TrailRequest;
import com.dangdang.tb_mong.trail.service.TrailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity saveTrail(@RequestParam Long userId,
                                    @RequestBody TrailRequest trailRequest){
        trailService.saveTrail(userId, trailRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/load")
    @Operation(summary = "산책로 불러오기")
    public TrailRequest getTrail(@RequestParam Long userId,
                                      @RequestParam Long trailId){
        return trailService.getTrail(userId, trailId);
    }

    @GetMapping(value = "/{trailId}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "산책로 이미지 조회하기")
    public Resource getTrailImage(@RequestParam Long userId,
                                  @PathVariable Long trailId) {
        return trailService.getTrailImage(userId, trailId);
    }

    @PostMapping(value = "/{trailId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "산책로 이미지 저장")
    public ResponseEntity saveTrailImage(@RequestParam MultipartFile file,
                                         @RequestParam Long userId,
                                         @PathVariable Long trailId) {
        trailService.saveTrailImage(file, userId, trailId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
