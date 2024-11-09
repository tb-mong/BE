package com.dangdang.tb_mong.common.Service;

import com.dangdang.tb_mong.common.entity.RepreCharacter;
import com.dangdang.tb_mong.common.entity.UserCharacter;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.RepreCharacterRepository;
import com.dangdang.tb_mong.common.repository.UserCharacterRepository;
import com.dangdang.tb_mong.common.repository.UserRepository;
import com.dangdang.tb_mong.common.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final UserCharacterRepository userCharacterRepository;
    private final UserRepository userRepository;
    private final RepreCharacterRepository repreCharacterRepository;

    public Resource getImageByCharacterId(Long characterId) {
        UserCharacter userCharacter = userCharacterRepository.findById(characterId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_CHARACTER));

        try {
            // 이미지 URL에서 Resource를 생성
            Resource imageResource = new UrlResource(userCharacter.getCharacter().getImage());
            if (imageResource.exists() && imageResource.isReadable()) {
                // 이미지 파일을 바이트 배열로 변환하여 반환
                return imageResource;
            } else {
                throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.FILE_NOT_FOUND);
            }
        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Resource getImageByUserId(PrincipalDetails userDetails) {
        RepreCharacter repreCharacter = repreCharacterRepository.findByUserId(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        try {
            // 이미지 URL에서 Resource를 생성
            Resource imageResource = new UrlResource(repreCharacter.getImage());
            if (imageResource.exists() && imageResource.isReadable()) {
                // 이미지 파일을 바이트 배열로 변환하여 반환
                return imageResource;
            } else {
                throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.FILE_NOT_FOUND);
            }
        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
