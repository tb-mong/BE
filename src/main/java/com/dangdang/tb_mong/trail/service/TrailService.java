package com.dangdang.tb_mong.trail.service;

import com.dangdang.tb_mong.common.entity.*;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.*;
import com.dangdang.tb_mong.common.security.PrincipalDetails;
import com.dangdang.tb_mong.trail.dto.SpotDto;
import com.dangdang.tb_mong.trail.dto.TrailRequest;
import com.dangdang.tb_mong.trail.dto.TrailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrailService {
    private final TrailRepository trailRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final SpotRepository spotRepository;
    private final UserLocationSummaryRepository userLocationSummaryRepository;
    private final UserCharacterRepository userCharacterRepository;

    @Value("${file}")
    private String rootFilePath;

    public static List<SpotDto> removeRedundantPoints(List<SpotDto> points, double threshold) {
        List<SpotDto> filtered = new ArrayList<>();
        filtered.add(points.get(0)); // 첫 번째 점 추가

        for (int i = 1; i < points.size(); i++) {
            SpotDto current = points.get(i);
            SpotDto lastAdded = filtered.get(filtered.size() - 1);

            // 두 점 간의 거리 계산
            double distance = haversine(lastAdded.getLa(), lastAdded.getLo(), current.getLa(), current.getLo());

            // 거리 기준을 초과하는 경우에만 추가
            if (distance > threshold) {
                filtered.add(current);
            }
        }

        filtered.add(points.get(points.size() - 1)); // 마지막 점 추가

        return filtered;
    }

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371000; // 지구 반지름 (미터)
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double deltaPhi = Math.toRadians(lat2 - lat1);
        double deltaLambda = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 거리 반환
    }

    public TrailResponse saveTrail(PrincipalDetails userDetails, TrailRequest trailRequest) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER));

        Location location = locationRepository.findByCode(trailRequest.getLocationCode())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_LOCATION));

        if (trailRequest.getSpotLists().isEmpty()){
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.EMPTY_SPOT_LIST);
        }

        Trail trail = Trail.builder()
                .name(trailRequest.getName())
                .image(null)
                .km(trailRequest.getKm())
                .pace(trailRequest.getPace())
                .time(trailRequest.getTime())
                .perHour(trailRequest.getPerHour())
                .likeCnt(0)
                .date(Date.valueOf(LocalDate.now()))
                .user(user)
                .location(location)
                .build();

        trailRepository.save(trail);

        user.updateCount();
        user.updateKm(trail.getKm());

        if (user.getExp() != 3){
            user.updateExp();
        }

        List<SpotDto> originalSpot = trailRequest.getSpotLists();

        double threshold = 5.0; // 거리 기준 (미터)
        List<SpotDto> filteredSpot = removeRedundantPoints(originalSpot, threshold); // 불필요한 점 제거

        for(int i = 0; i < filteredSpot.size(); i++){
            Spot spot = Spot.builder()
                    .la(BigDecimal.valueOf(filteredSpot.get(i).getLa()))
                    .lo(BigDecimal.valueOf(filteredSpot.get(i).getLo()))
                    .trail(trail)
                    .build();
            spotRepository.save(spot);
        }

        try {
            UserLocationSummary userLocationSummary = userLocationSummaryRepository.findByUserIdAndLocationId(user.getId(), location.getId());

            userLocationSummary.updateCount();
            userLocationSummary.updateKm(trail.getKm());

            userLocationSummaryRepository.save(userLocationSummary);

        } catch (NullPointerException e){
            UserLocationSummary newUserLocationSummary = UserLocationSummary.builder()
                    .count(1)
                    .km(trail.getKm())
                    .location(location)
                    .user(user)
                    .build();

            userLocationSummaryRepository.save(newUserLocationSummary);
        }

        return new TrailResponse(trail.getId());
    }

    public TrailRequest getTrail(PrincipalDetails userDetails, Long trailId) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER));

        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_TRAIL));

        List<Spot> spots = spotRepository.findAllByTrailId(trailId);

        if (spots.isEmpty()){
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.EMPTY_SPOT_LIST);
        }

        List<SpotDto> spotDtos = new ArrayList<>();

        for (int i = 0; i<spots.size(); i++){
            SpotDto dto = new SpotDto(spots.get(i).getLo().doubleValue(), spots.get(i).getLa().doubleValue());
            spotDtos.add(dto);
        }

        return new TrailRequest(trail.getName(),
                trail.getKm(),
                trail.getPace(),
                trail.getTime(),
                trail.getPerHour(),
                trail.getLocation().getCode(),
                spotDtos);
    }

    public Resource getTrailImage(PrincipalDetails userDetails, Long trailId) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER));

        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_TRAIL));

        // 이미지 파일이 있는지 확인
        if (!StringUtils.hasText(trail.getImage())) {
            throw new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_IMAGE);
        }

        try {
            Path path = Paths.get(rootFilePath, trail.getImage()).toAbsolutePath();
            Resource imageResource = new UrlResource(path.toUri());

            // 파일이 존재하고 읽을 수 있는지 확인
            if (imageResource.exists() && imageResource.isReadable()) {
                return imageResource;
            } else {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.IMAGE_READ_FAILED);
            }

        } catch (MalformedURLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.IMAGE_READ_FAILED);
        }
    }

    public void saveTrailImage(MultipartFile file, PrincipalDetails userDetails, Long trailId) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER));

        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_TRAIL));

        try {
            // 기존 이미지 파일 삭제
            if (StringUtils.hasText(trail.getImage())) {
                Path oldPath = Paths.get(rootFilePath, trail.getImage()).toAbsolutePath();
                Files.deleteIfExists(oldPath);
            }

            // 새로운 이미지 파일 저장
            String newImageName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path newPath = Paths.get(rootFilePath, newImageName).toAbsolutePath();
            Files.createDirectories(newPath.getParent());
            file.transferTo(newPath.toFile());

            trail.imageUpdate(newImageName);

            trailRepository.save(trail);

        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.IMAGE_UPDATE_FAILED);
        }
    }
}
