package com.dangdang.tb_mong.dict.controller;

import com.dangdang.tb_mong.common.Service.ImageService;
import com.dangdang.tb_mong.common.security.PrincipalDetails;
import com.dangdang.tb_mong.dict.dto.CharacterResponse;
import com.dangdang.tb_mong.dict.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dict")
@Tag(name = "캐릭터 도감", description = "캐릭터 목록 조회 및 대표 캐릭터 설정 API")
public class DictController {

    private final DictService dictService;
    private final ImageService imageService;

    @GetMapping("")
    @Operation(summary = "도감 조회")
    public List<CharacterResponse> getCharacterList(@AuthenticationPrincipal PrincipalDetails userDetails){
        return dictService.getCharacterList(userDetails);
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "캐릭터 이미지 조회")
    public Resource getImage(@RequestParam Long characterId) {
        return imageService.getImageByCharacterId(characterId);
    }

    @PostMapping("/set-repre")
    @Operation(summary = "대표 캐릭터 설정")
    public CharacterResponse setRepreCharacter(@AuthenticationPrincipal PrincipalDetails userDetails,
                                               @RequestParam Long characterId){
        return dictService.setRepreCharacter(userDetails, characterId);
    }
}
