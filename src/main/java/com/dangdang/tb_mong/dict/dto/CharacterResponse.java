package com.dangdang.tb_mong.dict.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CharacterResponse {
    private Long id;
    private String imageUrl;
    private Boolean isRepresentative;
    private Boolean unlocked;
}
