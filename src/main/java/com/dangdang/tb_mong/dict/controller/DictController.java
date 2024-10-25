package com.dangdang.tb_mong.dict.controller;

import com.dangdang.tb_mong.dict.dto.CharacterResponse;
import com.dangdang.tb_mong.dict.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dict")
@Tag(name = "캐릭터 도감", description = "")
public class DictController {

    private final DictService dictService;

    @GetMapping("")
    @Operation(summary = "도감 조회")
    public List<CharacterResponse> getCharacterList(@RequestParam Long userId){
        return dictService.getCharacterList(userId);
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "캐릭터 이미지 조회")
    public Resource getCafeImage(@RequestParam Long characterId) {
        return dictService.getImage(characterId);
    }

    @PostMapping("/set-repre")
    @Operation(summary = "대표 캐릭터 설정")
    public CharacterResponse setRepreCharacter(@RequestParam Long userId,
                                               @RequestParam Long characterId){
        return dictService.setRepreCharacter(userId, characterId);
    }
}
