package com.dangdang.tb_mong.dict.service;

import com.dangdang.tb_mong.common.entity.RepreCharacter;
import com.dangdang.tb_mong.common.entity.User;
import com.dangdang.tb_mong.common.entity.UserCharacter;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.RepreCharacterRepository;
import com.dangdang.tb_mong.common.repository.UserCharacterRepository;
import com.dangdang.tb_mong.common.repository.UserRepository;
import com.dangdang.tb_mong.common.security.PrincipalDetails;
import com.dangdang.tb_mong.dict.dto.CharacterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DictService {

    private final UserRepository userRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final RepreCharacterRepository repreCharacterRepository;

    public List<CharacterResponse> getCharacterList(PrincipalDetails userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER));

        // 모든 유저 캐릭터 정보
        List<UserCharacter> characters = userCharacterRepository.findAllByUserId(user.getId());

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

    public CharacterResponse setRepreCharacter(PrincipalDetails userDetails, Long characterId) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER));

        // 선택한 캐릭터 조회
        UserCharacter newRepreCharacter = userCharacterRepository.findById(characterId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_CHARACTER));

        // unlocked되지 않은 캐릭터인 경우 예외 발생
        if (!newRepreCharacter.getUnlocked()) {
            throw new CustomException(HttpStatus.FORBIDDEN, ErrorCode.CHARACTER_NOT_UNLOCKED);
        }

        // 기존 대표 캐릭터 해제
        RepreCharacter currentRepreCharacter = repreCharacterRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER));

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

        // 데이터 저장
        repreCharacterRepository.save(repreCharacter);

        // 응답 객체 생성
        CharacterResponse response = new CharacterResponse(newRepreCharacter.getCharacter().getId(), newRepreCharacter.getCharacter().getImage(), newRepreCharacter.getIsRepresentative(), newRepreCharacter.getUnlocked());
        return response;
    }
}
