package com.dangdang.tb_mong.home.service;

import com.dangdang.tb_mong.common.entity.Trail;
import com.dangdang.tb_mong.common.entity.User;
import com.dangdang.tb_mong.common.entity.UserCharacter;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.TrailRepository;
import com.dangdang.tb_mong.common.repository.UserCharacterRepository;
import com.dangdang.tb_mong.common.repository.UserRepository;
import com.dangdang.tb_mong.common.security.PrincipalDetails;
import com.dangdang.tb_mong.home.dto.UserInfoResponse;
import com.dangdang.tb_mong.home.dto.UserLevelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final UserRepository userRepository;
    private final TrailRepository trailRepository;
    private final UserCharacterRepository userCharacterRepository;

    public UserLevelResponse getLevel(PrincipalDetails userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER));

        UserLevelResponse dto = new UserLevelResponse(user.getLevel(), user.getExp());

        return dto;
    }

    public UserLevelResponse levelupEvent(PrincipalDetails userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER));

        if (user.getExp().equals(3)){
            user.levelup();
//            user.resetExp();
        }

        List<UserCharacter> userCharacters = userCharacterRepository.findAllByUserId(user.getId());

        for(int i = 0; i<user.getLevel(); i++){
            if (!userCharacters.get(i).getUnlocked()){
                userCharacters.get(i).setLocked();
                userCharacterRepository.save(userCharacters.get(i));
            }
        }

        userRepository.save(user);

        UserLevelResponse dto = new UserLevelResponse(user.getLevel(), user.getExp());

        return dto;
    }

    public UserInfoResponse getUserInfo(PrincipalDetails userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER));

        double totalKm = user.getTotal_km().doubleValue();

        int totalCount = user.getTotal_count();

        LocalDate today = LocalDate.now();

        List<Trail> todayWalks = trailRepository.findByUserIdAndDate(user.getId(), today);
        
        if (todayWalks.isEmpty()){
            UserInfoResponse emptyDto = new UserInfoResponse(0, 0.0, totalCount, totalKm);
            return emptyDto;
        }

        double todayKm = todayWalks.stream()
                .map(Trail::getKm)           // BigDecimal 객체로 가져오기
                .map(BigDecimal::doubleValue) // double 값으로 변환
                .mapToDouble(Double::doubleValue)
                .sum();

        int todayCount = todayWalks.size();

        return new UserInfoResponse(todayCount, todayKm, totalCount, totalKm);
    }
}
