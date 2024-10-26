package com.dangdang.tb_mong.home.service;

import com.dangdang.tb_mong.common.entity.User;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.UserRepository;
import com.dangdang.tb_mong.home.dto.UserInfoResponse;
import com.dangdang.tb_mong.home.dto.UserLevelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final UserRepository userRepository;

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

//    public UserInfoResponse getUserInfo(Long userId) {
//        return null;
//    }
}
