package com.dangdang.tb_mong.dongne.service;

import com.dangdang.tb_mong.common.entity.Location;
import com.dangdang.tb_mong.common.entity.Trail;
import com.dangdang.tb_mong.common.entity.User;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.LocationRepository;
import com.dangdang.tb_mong.common.repository.TrailRepository;
import com.dangdang.tb_mong.common.repository.UserRepository;
import com.dangdang.tb_mong.dongne.dto.LocationNameResponse;
import com.dangdang.tb_mong.common.dto.TrailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DongneService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final TrailRepository trailRepository;

    public LocationNameResponse getLocationName(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        String locationCode = user.getLocation().getCode();

        Location location = locationRepository.findByCode(locationCode)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_LOCATION));

        LocationNameResponse locationResponse = new LocationNameResponse(location.getId(), location.getName());

        return locationResponse;
    }

    public String getTopUser(Long userId, Long locationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        List<User> users = userRepository.findByLocationId(locationId);

        int max = 0;
        int maxIndex = 0;

        for (int i = 0; i< users.size(); i++){
            if (users.get(i).getCount() >= max){
                maxIndex = i;
                max = users.get(i).getCount();
            }
        }

        return users.get(maxIndex).getNickname();
    }

    public List<TrailDto> getTrailList(Long userId, Long locationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        List<Trail> trails = trailRepository.findByLocationId(locationId);

        List<TrailDto> dtos = trails.stream()
                .map(trail -> new TrailDto(
                        trail.getId(),
                        trail.getName(),
                        trail.getLocation().getName(),
                        trail.getKm(),
                        trail.getUser().getNickname(),
                        trail.getLikeCnt(),
                        trail.getImage()
                ))
                .collect(Collectors.toList());

        return dtos;
    }
}
