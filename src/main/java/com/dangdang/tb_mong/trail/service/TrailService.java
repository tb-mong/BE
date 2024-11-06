package com.dangdang.tb_mong.trail.service;

import com.dangdang.tb_mong.common.entity.*;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.*;
import com.dangdang.tb_mong.trail.dto.SpotDto;
import com.dangdang.tb_mong.trail.dto.TrailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Value("${file}")
    private String rootFilePath;

    public void saveTrail(Long userId, TrailRequest trailRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        Location location = locationRepository.findByCode(trailRequest.getLocationCode())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_LOCATION));

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

        List<SpotDto> spotDtos = trailRequest.getSpotLists();

        for(int i = 0; i < spotDtos.size(); i++){
            Spot spot = Spot.builder()
                    .la(spotDtos.get(i).getLa())
                    .lo(spotDtos.get(i).getLo())
                    .trail(trail)
                    .build();
            spotRepository.save(spot);
        }

        try {
            UserLocationSummary userLocationSummary = userLocationSummaryRepository.findByLocationId(location.getId());

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
    }

    public TrailRequest getTrail(Long userId, Long trailId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TRAIL));

        List<Spot> spots = spotRepository.findAllByTrailId(trailId);
        List<SpotDto> spotDtos = new ArrayList<>();

        for (int i = 0; i<spots.size(); i++){
            SpotDto dto = new SpotDto(spots.get(i).getLo(), spots.get(i).getLa());
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

    public Resource getTrailImage(Long userId, Long trailId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TRAIL));

        // 이미지 파일이 있는지 확인
        if (!StringUtils.hasText(trail.getImage())) {
            throw new CustomException(HttpStatus.NOT_FOUND, ErrorCode.IMAGE_NOT_FOUND);
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

    public void saveTrailImage(MultipartFile file, Long userId, Long trailId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TRAIL));

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
