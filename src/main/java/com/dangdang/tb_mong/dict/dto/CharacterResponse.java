package com.dangdang.tb_mong.dict.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@AllArgsConstructor
public class CharacterResponse {
    Long id;
    String imageUrl;
    Boolean isRepresentative;
    Boolean unlocked;
}
