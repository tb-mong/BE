package com.dangdang.tb_mong.dict.service;

import com.dangdang.tb_mong.common.entity.RepreCharacter;
import com.dangdang.tb_mong.common.entity.User;
import com.dangdang.tb_mong.common.entity.UserCharacter;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.RepreCharacterRepository;
import com.dangdang.tb_mong.common.repository.UserCharacterRepository;
import com.dangdang.tb_mong.common.repository.UserRepository;
import com.dangdang.tb_mong.dict.dto.CharacterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DictService {

    private final UserRepository userRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final RepreCharacterRepository repreCharacterRepository;

    public List<CharacterResponse> getCharacterList(Long userId) {
        User user = (User) userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        // 모든 유저 캐릭터 정보
        List<UserCharacter> characters = userCharacterRepository.findAllByUserId(userId);

        // UserCharacter 목록을 CharacterResponse DTO로 변환
        List<CharacterResponse> responses = characters.stream()
                .map(userCharacter -> new CharacterResponse(
                        userCharacter.getCharacter().getId(),
                        userCharacter.getCharacter().getImage(),
                        userCharacter.getIsRepresentative(),
                        userCharacter.getUnlocked()
                ))
                .collect(Collectors.toList());

        return responses;
    }


    public Resource getImage(Long characterId) {
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

    public CharacterResponse setRepreCharacter(Long userId, Long characterId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        // 선택한 캐릭터 조회
        UserCharacter newRepreCharacter = userCharacterRepository.findById(characterId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_CHARACTER));

        // unlocked되지 않은 캐릭터인 경우 예외 발생
        if (!newRepreCharacter.getUnlocked()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.CHARACTER_NOT_UNLOCKED);
        }

        // 기존 대표 캐릭터 해제
        RepreCharacter currentRepreCharacter = user.getRepreCharacter();
        if (currentRepreCharacter != null) {
            UserCharacter currentRepreUserCharacter = currentRepreCharacter.getUserCharacter();
            if (currentRepreUserCharacter != null) {
                currentRepreUserCharacter.setIsRepresentative(false);
            }
        }

        // 새로운 대표 캐릭터 설정
        newRepreCharacter.setIsRepresentative(true);

        // RepreCharacter 갱신
        RepreCharacter repreCharacter = currentRepreCharacter != null ? currentRepreCharacter : new RepreCharacter();
        repreCharacter.setUserCharacter(newRepreCharacter);
        user.setRepreCharacter(repreCharacter);

        // 데이터 저장
        repreCharacterRepository.save(repreCharacter);
        userRepository.save(user);

        // 응답 객체 생성
        CharacterResponse response = new CharacterResponse(newRepreCharacter.getCharacter().getId(), newRepreCharacter.getCharacter().getImage(), newRepreCharacter.getIsRepresentative(), newRepreCharacter.getUnlocked());
        return response;
    }
}
