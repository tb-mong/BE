package com.dangdang.tb_mong.settings.service;

import com.dangdang.tb_mong.common.entity.Location;
import com.dangdang.tb_mong.common.entity.User;
import com.dangdang.tb_mong.common.entity.UserLocationSummary;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.LocationRepository;
import com.dangdang.tb_mong.common.repository.UserLocationSummaryRepository;
import com.dangdang.tb_mong.common.repository.UserRepository;
import com.dangdang.tb_mong.common.security.PrincipalDetails;
import com.dangdang.tb_mong.settings.dto.LocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SettingsService {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final UserLocationSummaryRepository userLocationSummaryRepository;

    public LocationResponse setLocation(PrincipalDetails userDetails, String locationCode) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        Location location = locationRepository.findByCode(locationCode)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_LOCATION));

        user.setLocation(location);

        userRepository.save(user);

        UserLocationSummary userLocationSummary = UserLocationSummary.builder()
                .km(BigDecimal.valueOf(0.0))
                .count(0)
                .user(user)
                .location(location)
                .build();

        userLocationSummaryRepository.save(userLocationSummary);

        LocationResponse dto = new LocationResponse(location.getName(), location.getCode());

        return dto;
    }

    public String setNickname(PrincipalDetails userDetails, String newNickname) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        user.setNickname(newNickname);

        userRepository.save(user);

        return newNickname;
    }
}
