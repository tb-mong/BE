package com.dangdang.tb_mong.home.service;

import com.dangdang.tb_mong.common.entity.Trail;
import com.dangdang.tb_mong.common.entity.User;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.TrailRepository;
import com.dangdang.tb_mong.common.repository.UserRepository;
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

    public UserLevelResponse getLevel(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        UserLevelResponse dto = new UserLevelResponse(user.getLevel(), user.getExp());

        return dto;
    }

    public UserLevelResponse levelupEvent(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        if (user.getExp().equals(3)){
            user.levelup();
        }

        userRepository.save(user);

        UserLevelResponse dto = new UserLevelResponse(user.getLevel(), user.getExp());

        return dto;
    }

    public UserInfoResponse getUserInfo(Long userId) {
        LocalDate today = LocalDate.now();

        List<Trail> todayWalks = trailRepository.findByUserIdAndDate(userId, today);

        double todayKm = todayWalks.stream()
                .map(Trail::getKm)           // BigDecimal 객체로 가져오기
                .map(BigDecimal::doubleValue) // double 값으로 변환
                .mapToDouble(Double::doubleValue)
                .sum();

        int todayCount = todayWalks.size();

        int totalWalkCount = trailRepository.countByUserId(userId);

        return new UserInfoResponse(todayCount, todayKm, totalWalkCount);
    }
}
