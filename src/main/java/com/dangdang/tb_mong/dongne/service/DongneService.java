package com.dangdang.tb_mong.dongne.service;

import com.dangdang.tb_mong.common.entity.LikeTrail;
import com.dangdang.tb_mong.common.entity.Location;
import com.dangdang.tb_mong.common.entity.Trail;
import com.dangdang.tb_mong.common.entity.User;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.LikeTrailRepository;
import com.dangdang.tb_mong.common.repository.LocationRepository;
import com.dangdang.tb_mong.common.repository.TrailRepository;
import com.dangdang.tb_mong.common.repository.UserRepository;
import com.dangdang.tb_mong.dongne.dto.LocationNameResponse;
import com.dangdang.tb_mong.common.dto.TrailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DongneService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final TrailRepository trailRepository;
    private final LikeTrailRepository likeTrailRepository;

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
            if (users.get(i).getTotal_count() >= max){
                maxIndex = i;
                max = users.get(i).getTotal_count();
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

    public List<TrailDto> getSearchByLocation(Long userId, Long locationId, String trailSortOption, String keyword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        if (keyword.equals("") || keyword.equals(null) || keyword.equals(" ")){
            keyword = "ALL";
        }

        List<Trail> trails;

        if (keyword.equals("ALL")){
            trails = trailRepository.findByLocationId(locationId);
        }else{
            trails = trailRepository.findByLocationIdAndNameContaining(locationId, keyword);
        }

        switch (trailSortOption){
            case "LIKE":
                trails = trails.stream()
                        .sorted((t1, t2) -> t2.getLikeCnt().compareTo(t1.getLikeCnt()))
                        .collect(Collectors.toList());
                break;

            case "RECENT":
                trails = trails.stream()
                        .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                        .collect(Collectors.toList());
                break;

            case "MY_LIKES":
                trails = trails.stream()
                        .filter(trail -> likeTrailRepository.existsByUserAndTrail(user, trail))
                        .collect(Collectors.toList());
                break;

            case "MY_WALKS":
                trails = trails.stream()
                        .filter(trail -> trail.getUser().getId().equals(userId))
                        .collect(Collectors.toList());
                break;

            default:
                throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_SORT_OPTION);

        }

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

    @Transactional
    public TrailDto likeTrail(Long userId, Long trailId) {
        // 유저와 산책로 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TRAIL));

        // 이미 좋아요를 눌렀는지 확인
        boolean alreadyLiked = likeTrailRepository.existsByUserAndTrail(user, trail);
        if (alreadyLiked) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.ALREADY_LIKED_TRAIL);
        }

        // 좋아요 추가
        LikeTrail likeTrail = new LikeTrail();
        likeTrail.setUser(user);
        likeTrail.setTrail(trail);
        likeTrailRepository.save(likeTrail);

        // 좋아요 수 증가
        trail.setLikeCnt(trail.getLikeCnt() + 1);
        trailRepository.save(trail);

        TrailDto trailDto = TrailDto.builder()
                .id(trail.getId())
                .name(trail.getName())
                .location_name(trail.getLocation().getName())
                .km(trail.getKm())
                .nickname(trail.getUser().getNickname())
                .like_count(trail.getLikeCnt())
                .image(trail.getImage())
                .build();

        return trailDto;
    }
}
